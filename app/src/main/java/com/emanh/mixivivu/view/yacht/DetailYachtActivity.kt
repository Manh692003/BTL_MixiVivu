@file:Suppress("DEPRECATION")

package com.emanh.mixivivu.view.yacht

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emanh.mixivivu.R
import com.emanh.mixivivu.databinding.ActivityDetailYachtBinding
import com.emanh.mixivivu.databinding.InformationDetailYachtBinding
import com.emanh.mixivivu.model.InformationYachtModel
import com.emanh.mixivivu.model.YachtModel
import com.emanh.mixivivu.view.BaseActivity
import java.text.NumberFormat
import java.util.Locale

class DetailYachtActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailYachtBinding
    private lateinit var yachtModel: YachtModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailYachtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initListPic()
        initDetailYacht()
    }

    private fun init() {
        yachtModel = intent.getParcelableExtra("objectYacht")!!

        binding.buttonBack.setOnClickListener { finish() }

        val buttons = mapOf(
            binding.characteristic to CharacteristicFragment(),
            binding.priceRoom to PriceRoomFragment(),
            binding.intro to IntroFragment(),
            binding.evaluation to EvaluationFragment()
        )

        buttons.forEach { (button, fragment) ->
            button.setOnClickListener {
                replaceFragment(fragment)
                updateButtonBackgrounds(button)
            }
        }
    }

    private fun initListPic() {
        val colorList =  ArrayList<String>()

        for (imageUrl in yachtModel.picUrl) {
            colorList.add(imageUrl)
        }

        Glide.with(this)
            .load(colorList[0])
            .into(binding.picMain)

        binding.listPic.adapter = PicListAdapter(colorList, binding.picMain)
        binding.listPic.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    @SuppressLint("SetTextI18n")
    private fun initDetailYacht() {
        binding.title.text = "Du thuyền ${yachtModel.title}"
        binding.price.text = "${formatPrice(yachtModel.price)}đ/ khách"

        val infoYachtItem = listOf(
            InformationYachtModel(binding.launching, R.drawable.anchor, "Hạ thủy", 0),
            InformationYachtModel(binding.cabin, R.drawable.bed, "Cabin", 1),
            InformationYachtModel(binding.body, R.drawable.ship, "Thân vỏ", 2),
            InformationYachtModel(binding.trip, R.drawable.marker, "Hành trình", 3),
            InformationYachtModel(binding.operating, R.drawable.briefcase, "Điều hành", 4)
        )

        infoYachtItem.forEachIndexed { _, item ->
            setupInfoYacht(item.binding, item.iconRes, item.title, item.label)
        }
    }

    private fun updateButtonBackgrounds(selectedButton: View) {
        val buttons = listOf(
            binding.characteristic,
            binding.priceRoom,
            binding.intro,
            binding.evaluation
        )

        buttons.forEach { button ->
            val backgroundRes = if (button == selectedButton) {
                R.drawable.bg_button_list1
            } else {
                R.drawable.bg_button_list2
            }
            button.setBackgroundResource(backgroundRes)
        }
    }

    private fun setupInfoYacht(itemBinding: InformationDetailYachtBinding, iconRes: Int, textTitle: String, idInfoShip: Int) {
        itemBinding.apply {
            icon.setImageResource(iconRes)
            title.text = textTitle
            label.text = yachtModel.infoShip[idInfoShip]
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayoutDetail, fragment)
        fragmentTransaction.commit()
    }

    private fun formatPrice(price: Int): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(price)
    }
}