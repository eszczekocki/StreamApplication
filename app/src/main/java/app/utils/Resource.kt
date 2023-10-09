package app.utils

data class Resource<out T>
    (val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T? = null, message: String? = null): Resource<T> =
            Resource(status = Status.FAILURE, data = data, message = message)

        fun <T> loading(data: T? = null): Resource<T> =
            Resource(status = Status.LOADING, data = data, message = null)
    }

}