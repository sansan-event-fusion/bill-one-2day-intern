package com.sansan.billone.lib


sealed interface ApplicationResult {
  data object Success : ApplicationResult
  data class Failure(val reason: String) : ApplicationResult

  companion object {
    fun success(): ApplicationResult = Success
    fun failure(reason: String): ApplicationResult = Failure(reason)
  }

  fun isSuccess(): Boolean = this is Success
  fun isFailure(): Boolean = this is Failure

}
