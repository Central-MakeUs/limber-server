package club.cmc.limber.domain.timer.enums;

public enum TimerStatus {
    READY,      // 예약됨 (시작 전)
    RUNNING,    // 진행 중
    PAUSED,     // 일시정지
    COMPLETED,  // 종료됨
    CANCELED,    // 취소됨

    // 중단
    // 완료
}
