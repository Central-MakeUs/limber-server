package club.cmc.limber.domain.timerhistory.dto.history;

import java.time.LocalTime;

public interface FocusTimeSlice {
    Long getFocusTypeId();
    String getFocusTypeTitle();
    LocalTime getActualStartTime();
    LocalTime getActualEndTime();
}
