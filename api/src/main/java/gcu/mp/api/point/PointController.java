package gcu.mp.api.point;

import gcu.mp.service.point.dto.GetPointRes;
import gcu.mp.common.api.BaseResponse;
import gcu.mp.common.api.BaseResponseStatus;
import gcu.mp.service.point.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "포인트")
@RequestMapping(value = "/point")
public class PointController {
    private final PointService pointService;
    @Operation(summary = "포인트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2103", description = "존재하지 않는 유저입니다.", content = @Content),
            @ApiResponse(responseCode = "4001", description = "서버 오류입니다.", content = @Content)
    })
    @GetMapping("")
    public ResponseEntity<BaseResponse<GetPointRes>> getPoint() {
        try {
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            Long memberId = Long.parseLong(loggedInUser.getName());
            GetPointRes getPointRes = pointService.getPoint(memberId);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(getPointRes));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(BaseResponseStatus.SERVER_ERROR));
        }
    }
}
