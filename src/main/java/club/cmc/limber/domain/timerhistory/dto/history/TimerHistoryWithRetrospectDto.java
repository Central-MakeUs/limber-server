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

                // 날짜 파트 (오늘 / N일 전)
                LocalDate baseDate = historyDt.toLocalDate();
                LocalDate today = LocalDate.now(zone);
                long daysDiff = ChronoUnit.DAYS.between(baseDate, today);
                String dayPart = (daysDiff == 0) ? "오늘" : daysDiff + "일 전";

                // 시간 파트 (자정 경과 및 DST 안전 처리)
                ZonedDateTime start = ZonedDateTime.of(baseDate, startTime, zone);
                ZonedDateTime end   = ZonedDateTime.of(baseDate, endTime, zone);
                if (end.isBefore(start)) {
                        end = end.plusDays(1); // 자정 넘김
                }

                long minutes = Duration.between(start, end).toMinutes(); // 항상 0 이상
                long hours = minutes / 60;
                long mins  = minutes % 60;

                StringBuilder timePart = new StringBuilder();
                if (hours > 0) timePart.append(hours).append("시간 ");
                timePart.append(mins).append("분");

                return dayPart + ", " + timePart;
        }

}
