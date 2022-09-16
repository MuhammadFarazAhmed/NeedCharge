package incubasys.needcharge.base.utils

//We use this to remove the Fragment only when the animation finished
interface Dismissible {
    fun dismiss(listener: OnDismissedListener)

    interface OnDismissedListener {
        fun onDismissed()
    }
}
