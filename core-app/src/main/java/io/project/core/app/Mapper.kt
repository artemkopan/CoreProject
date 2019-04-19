package io.project.core.app

@Suppress("MemberVisibilityCanBePrivate", "unused", "UNUSED_PARAMETER")
abstract class Mapper<To, From> {

    fun map(from: From): To = map(from, Unit)

    abstract fun map(from: From, params: Any?): To

    fun reverseMap(to: To): From = reverseMap(to, Unit)

    open fun reverseMap(to: To, params: Any?): From {
        throw notImplementedException()
    }

    fun notImplementedException(): NoSuchMethodError {
        return NoSuchMethodError("The method is not implemented")
    }

    fun mapList(typeList: List<From>): List<To> = mapList(typeList, Unit)

    open fun mapList(typeList: List<From>, params: Any? = null): List<To> {

        val list = ArrayList<To>()

        for (type in typeList) {

            list.add(map(type, params))

        }

        return list
    }

    open fun reverseMapList(typeList: List<To>, params: Any?): List<From> {

        val list = ArrayList<From>()

        for (type in typeList) {

            list.add(reverseMap(type, params))

        }

        return list

    }

}

