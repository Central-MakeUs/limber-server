package club.cmc.limber.domain.timerhistory.enums;

public enum FailReason {
    LACK_OF_FOCUS_INTENTION("집중 의지가 부족해요"),
    NEED_BREAK("휴식이 필요해요"),
    FINISHED_EARLY("일정이 빨리 끝났어요"),
    EMERGENCY("긴급한 상황이 발생했어요"),
    EXTERNAL_DISTURBANCE("외부의 방해가 있어요"),
    NONE("에러가 발생하였습니다");

    private final String description;

    FailReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

