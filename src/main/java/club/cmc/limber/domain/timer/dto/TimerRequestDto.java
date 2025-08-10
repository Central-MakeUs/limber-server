package club.cmc.limber.domain.timer.dto;

import club.cmc.limber.domain.timer.enums.RepeatCycleCode;
import club.cmc.limber.domain.timer.enums.TimerCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

/**
 * 타이머 즉시 시작 요청 DTO
 */
@Schema(name = "TimerRequest", description = "타이머 요청")
public record TimerRequestDto(
        @NotNull
        @Schema(description = "사용자 UUID", example = "UUID", requiredMode = Schema.RequiredMode.REQUIRED)
        Long userId,

        @NotBlank
        @Size(max = 50)
        @Schema(description = "타이머 제목", example = "Deep Work", maxLength = 50, requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @NotNull
        @Schema(description = "집중 유형 ID", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
        Long focusTypeId,

        @NotNull
        @Schema(description = "타이머 타입 코드", example = "IMMEDIATE", requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"IMMEDIATE", "SCHEDULED"})
        TimerCode timerCode,

        @NotNull
        @Schema(description = "반복 주기 코드", example = "WEEKLY", requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"NONE","EVERY","WEEKDAY","WEEKEND"})
        RepeatCycleCode repeatCycleCode,

        @Schema(description = "반복 요일(0:일, 6:월)", example = "0,1,2,3,4,5,6")
        String repeatDays,

        @Schema(description = "시작 시각(HH:mm)", example = "19:00", type = "string", format = "time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startTime,

        @Schema(description = "종료 시각(HH:mm)", example = "21:00", type = "string", format = "time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime endTime
) {}

