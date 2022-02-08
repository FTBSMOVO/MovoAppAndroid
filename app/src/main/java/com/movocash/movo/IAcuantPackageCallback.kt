package com.movocash.movo

interface IAcuantPackageCallback {

    fun onInitializeSuccess()

    fun onInitializeFailed(error: List<Error>)
}