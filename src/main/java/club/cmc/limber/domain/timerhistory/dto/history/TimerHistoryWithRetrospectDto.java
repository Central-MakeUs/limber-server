package club.cmc.limber.domain.timerhistory.dto.history;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timerhistory.enums.FailReason;
import club.cmc.limber.domain.timerhistory.enums.HistoryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.*;
import java.time.temporal.ChronoUnit;

public record TimerHistoryWithRetrospectDto(
        Long id,
        Long timerId,
        String userId,
        String title,
        Long focusTypeId,
        RepeatCycleCode repeatCycleCode,
        String repeatDays,
        LocalDateTime historyDt,
        HistoryStatus historyStatus,
        FailReason failReason,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @DateTimeFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "19:00")
        LocalTime startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @DateTimeFormat(pattern = "HH:mm")
        @Schema(type = "string", example = "21:00")
        LocalTime endTime,

        boolean hasRetrospect,
        Long retrospectId,
        Integer retrospectImmersion,
        String retrospectComment,
        String focusTypeTitle,

        // ★ DB에서 null로 받지만, 생성자에서 계산해 채운다
        String retrospectSummary
) {
        // compact constructor: 모든 필드에 명시적으로 할당
        public TimerHistoryWithRetrospectDto {
                // 나머지 필드는 전달값 그대로
                // 마지막 요약 문자열만 직접 계산해 덮어쓴다
                retrospectSummary = buildRetrospectSummary(historyDt, startTime, endTime, ZoneId.of("Asia/Seoul"));
        }

        private static String buildRetrospectSummary(
                LocalDateTime historyDt, LocalTime startTime, LocalTime endTime, ZoneId zone
        ) {
                if (historyDt == null || startTime == null || endTime == null) return "";

                // 날짜 파트: 오늘/ N일 전 (한 달 넘어가도 N일 전 유지)
                LocalDate today = LocalDate.now(zone);
                long daysDiff = ChronoUnit.DAYS.between(historyDt.toLocalDate(), today);
                String dayPart = (daysDiff == 0) ? "오늘" : daysDiff + "일 전";

                // 시간 파트: start~end 소요시간 (자정 넘어가는 케이스 처리)
                long minutes;
                if (endTime.isBefore(startTime)) {
                        // 자정 경과: end + 24h - start
                        minutes = Duration.between(startTime, endTime.plusHours(24)).toMinutes();
                } else {
                        minutes = Duration.between(startTime, endTime).toMinutes();
                }
                long hours = minutes / 60;
                long mins = minutes % 60;

                StringBuilder timePart = new StringBuilder();
                if (hours > 0) timePart.append(hours).append("시간 ");
                timePart.append(mins).append("분");

                return dayPart + ", " + timePart;
        }
}
