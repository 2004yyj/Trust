package kr.hs.dgsw.data.base

abstract class NoCacheDataSource<RE> {
    abstract val remote: RE
}