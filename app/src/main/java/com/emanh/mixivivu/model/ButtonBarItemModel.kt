package com.emanh.mixivivu.model

import com.emanh.mixivivu.databinding.ButtonBarItemBinding

data class ButtonBarItemModel(
    val binding: ButtonBarItemBinding,
    val iconRes: Int,
    val text: String,
    val position: Int
)
