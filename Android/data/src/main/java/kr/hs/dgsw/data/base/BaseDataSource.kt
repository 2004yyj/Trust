package kr.hs.dgsw.data.base

abstract class BaseDataSource<RE, CH> {
    abstract val remote: RE
    abstract val cache: CH
}