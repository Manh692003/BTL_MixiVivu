package com.emanh.mixivivu.model

import android.widget.ArrayAdapter
import com.emanh.mixivivu.databinding.InputSearchBinding

data class InputSearchModel(
    val binding: InputSearchBinding,
    val adapter: ArrayAdapter<String>,
    val initialSelection: String?
)
