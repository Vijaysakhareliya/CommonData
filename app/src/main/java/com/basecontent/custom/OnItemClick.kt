package com.basecontent.custom

interface OnItemClick<T> {
    fun onItemClick(data: T, position: Int)
}