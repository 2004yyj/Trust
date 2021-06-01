package kr.hs.dgsw.domain.base

abstract class BaseUseCase<out T> {
    abstract fun buildUseCaseObservable(): T
}