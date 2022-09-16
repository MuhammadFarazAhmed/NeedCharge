package incubasys.needcharge.datainterfaces.models


data class ApiResponse<T> constructor(var apiStatus: ApiStatus, var data: T? = null, var error: Message? = null) {

    fun data(data: T?): ApiResponse<T> {

        this.data = data
        return this
    }

    fun error(error: Message): ApiResponse<T> {
        this.error = error
        return this
    }

    fun status(apiStatus: ApiStatus): ApiResponse<T> {
        this.apiStatus = apiStatus
        return this
    }
}
