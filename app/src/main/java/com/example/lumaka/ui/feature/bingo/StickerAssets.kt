package com.example.lumaka.ui.feature.bingo

import com.example.lumaka.R

object StickerAssets {
    /**
     * Returns the drawable resource ID for a given sticker ID.
     * - 1..29 map to sticker_redpanda_01..29
     * - 101..131 map to sticker_skzoo_01..31
     */
    fun resIdFor(id: Int): Int? {
        val name = when {
            id in 1..29 -> "sticker_redpanda_%02d".format(id)
            id in 101..131 -> "sticker_skzoo_%02d".format(id - 100)
            else -> return null
        }
        return try {
            R.drawable::class.java.getField(name).getInt(null)
        } catch (_: Throwable) {
            null
        }
    }
}
