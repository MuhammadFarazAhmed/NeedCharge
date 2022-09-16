package incubasys.needcharge.home.callback

import incubasys.needcharge.home.data.Menu

interface MenuItemCallback {
    fun onMenuItemClicked(menuItem : Menu)
}
