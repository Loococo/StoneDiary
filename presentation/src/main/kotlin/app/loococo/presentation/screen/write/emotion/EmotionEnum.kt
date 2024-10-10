package app.loococo.presentation.screen.write.emotion

import app.loococo.presentation.R


enum class EmotionEnum(val resId: Int) {
    HAPPY(R.drawable.ic_stone_happy),
    JOY(R.drawable.ic_stone_joy),
    WHAT(R.drawable.ic_stone_what),
    NO(R.drawable.ic_stone_no),
    ANGRY(R.drawable.ic_stone_angry),
    ANNOYING(R.drawable.ic_stone_annoying),
    SAD(R.drawable.ic_stone_sad),
    SICK(R.drawable.ic_stone_sick)
}

fun String.formatEmotionEnum(): EmotionEnum {
    return try {
        EmotionEnum.valueOf(this)
    } catch (e: IllegalArgumentException) {
        EmotionEnum.HAPPY
    }
}