package com.babiichuk.waterbalancetracker.core.utils

data class StateHolder<out T>(
    val value : T,
    val properties : MutableMap<State, Any> = mutableMapOf()
)

fun <T> List<T>.mapToStateHolderList(optional: ((T) -> MutableMap<State, Any>) = { mutableMapOf() }) =
    map { StateHolder(it, optional(it)) }

fun <T> List<T>.mapToStateHolderListIndexed(optional: ((Int, T) -> MutableMap<State, Any>) = { _, _ -> mutableMapOf() }) =
    mapIndexed { index, e -> StateHolder(e, optional(index, e)) }

val <T> List<StateHolder<T>>.values
    get() = map { it.value }

fun <T> StateHolder<T>.getStateOrFalse(state: State) = this.properties.getOrElse(state) { false } as Boolean

fun <T> childStateChange(
    newItem: T,
    properties: MutableMap<State, Any>
): StateHolder<T> {
    val mapState = properties.toMutableMap()
    mapState[State.CHILD_CHANGE] = !(properties.getOrElse(State.CHILD_CHANGE) { false } as Boolean)
    return StateHolder(newItem, mapState)
}

fun <T> StateHolder<T>.propertyChange(
    state: State,
    block: () -> Boolean,
): StateHolder<T> {
    val properties = this.properties.toMutableMap()
    properties[state] = block.invoke()
    return childStateChange(this.value, properties)
}

fun <T> StateHolder<T>.propertyChange(
    state: State,
    value: Boolean
): StateHolder<T> {
    val properties = this.properties.toMutableMap()
    properties[state] = value
    return childStateChange(this.value, properties)
}

fun <T> StateHolder<T>.propertyChangeIf(
    state: State,
    value: Boolean,
    block: () -> Boolean
): StateHolder<T> {
    return if (block.invoke()){
        val properties = this.properties.toMutableMap()
        properties[state] = value
        childStateChange(this.value, properties)
    } else {
        this
    }
}

enum class State {
    EXPANDED,
    ENABLED,
    SELECTED,
    CHECKED,
    CHILD_CHANGE,
    VISIBLE,
    FAVORITE,
    SELECTABLE,
    FIXED
}