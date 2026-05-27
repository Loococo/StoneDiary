package app.loococo.presentation.theme

import androidx.compose.ui.unit.dp

/**
 * 디자인 토큰 — 스페이싱·반경·스트로크 스케일.
 *
 * UI 코드에서 `.dp` 하드코딩 금지. 항상 이 객체의 상수를 사용한다.
 * 새 값이 필요하면 먼저 이 파일에 토큰을 추가한 뒤 사용한다.
 */

/** 컴포넌트 간/내부 여백 스케일 */
object Spacing {
    val Space02 = 2.dp
    val Space04 = 4.dp
    val Space08 = 8.dp
    val Space10 = 10.dp
    val Space12 = 12.dp
    val Space16 = 16.dp
    val Space20 = 20.dp
    val Space24 = 24.dp
    val Space28 = 28.dp
    val Space32 = 32.dp
    val Space40 = 40.dp
    val Space48 = 48.dp
}

/** 모서리 반경 스케일 */
object Radius {
    val R4 = 4.dp
    val R8 = 8.dp
    val R12 = 12.dp
    val R16 = 16.dp
    val R20 = 20.dp
    val R24 = 24.dp
    val Full = 999.dp
}

/** 테두리/구분선 두께 스케일 */
object Stroke {
    val Hairline = 0.5.dp
    val Thin = 1.dp
    val Bold = 2.dp
    val Heavy = 3.dp
}

/** 주요 컴포넌트 고정 크기 */
object Component {
    /** 기본 CTA 버튼 높이 */
    val CtaHeight = 56.dp

    /** 보조 버튼 높이 */
    val ButtonHeight = 48.dp

    /** 리스트 아이템 최소 높이 */
    val ListItemMinHeight = 56.dp

    /** 아이콘 표준 크기 */
    val IconSize = 24.dp

    /** 작은 아이콘 */
    val IconSizeSmall = 18.dp
}
