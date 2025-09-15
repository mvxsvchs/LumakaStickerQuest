package com.example.lumaka.domain.model

import androidx.annotation.StringRes
import com.example.lumaka.R

enum class CategoryEnum (val id: Int, @StringRes val title: Int) {
    ALL (id = 0, title = R.string.category_all),
    GENERAL (id = 1, title = R.string.category_general),
    WORK (id = 2, title = R.string.category_work),
    SCHOOL (id = 3, title = R.string.category_school),
    HOUSEHOLD (id = 4, title = R.string.category_household),
    GYM (id = 5, title = R.string.category_gym)

}