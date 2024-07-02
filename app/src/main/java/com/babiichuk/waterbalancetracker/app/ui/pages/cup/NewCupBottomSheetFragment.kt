package com.babiichuk.waterbalancetracker.app.ui.pages.cup

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.babiichuk.waterbalancetracker.app.ui.adapterdelegates.addNewFooterAdapterDelegate
import com.babiichuk.waterbalancetracker.app.ui.adapterdelegates.beveragesAdapterDelegate
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.cup.dialog.CreateCupDialogFragment
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.AsyncListDiffDelegationAdapter
import com.babiichuk.waterbalancetracker.databinding.FragmentBottomSheetNewCupBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewCupBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentBottomSheetNewCupBinding::bind)
    private val viewModel by activityViewModels<NewCupViewModel>()

    companion object {
        const val TAG: String = "NewCupBottomSheetFragment"
        private const val ARG_INTERVAL_ID = "ARG_INTERVAL_ID"

        fun newInstance(intervalId: Int): NewCupBottomSheetFragment {
            return NewCupBottomSheetFragment().apply {
                arguments = bundleOf(ARG_INTERVAL_ID to intervalId)
            }
        }
    }

    var onDismissCallback: (() -> Unit)? = null

    private val beveragesAdapter by lazy {
        AsyncListDiffDelegationAdapter(
            beveragesAdapterDelegate { beveragesId -> viewModel.onBeveragesClicked(beveragesId) },
            addNewFooterAdapterDelegate { openCreateBeveragesDialog() }
        )
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behavior = BottomSheetBehavior.from(parentLayout)
                setupFullHeight(parentLayout)
                behavior.setPeekHeight(Resources.getSystem().displayMetrics.heightPixels, true)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: FrameLayout) {
        bottomSheet.updateLayoutParams<ViewGroup.LayoutParams> {
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBottomSheetNewCupBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        ).root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    private fun parseArgs() {
        if (arguments == null) return

        val intervalId = requireArguments().getInt(ARG_INTERVAL_ID)
        viewModel.intervalId = intervalId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupBinding()
        viewModel.subscribe()

    }

    private fun FragmentBottomSheetNewCupBinding.setupBinding() {
        tvBack.setOnClickListener { dismiss() }
        tvSave.setOnClickListener { saveAndDismiss() }
        rvCups.apply {
            itemAnimator = null
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = beveragesAdapter
        }
    }

    private fun NewCupViewModel.subscribe() {
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { entityFlow.collectLatest { entity -> beveragesAdapter.items = entity } }
        }
    }

    private fun saveAndDismiss() {
        viewModel.addCupsToInterval()
        dismiss()
    }

    private fun openCreateBeveragesDialog() {
        val fragment = CreateCupDialogFragment.newInstance()
        fragment.onDismissCallback = { viewModel.addNewBeverages() }
        fragment.show(parentFragmentManager, CreateCupDialogFragment.TAG)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissCallback?.invoke()
        super.onDismiss(dialog)
    }

}