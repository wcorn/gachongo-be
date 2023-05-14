package gcu.mp.api.map;

import gcu.mp.api.map.dto.request.PostMapPointReq;
import gcu.mp.api.map.mapper.MapMapper;
import gcu.mp.api.member.dto.request.ModifyNicknameRequest;
import gcu.mp.common.api.BaseResponse;
import gcu.mp.common.api.BaseResponseStatus;
import gcu.mp.service.map.MapService;
import gcu.mp.service.map.dto.GetMapPointDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "유저")
@RequestMapping(value = "/map")
public class MapController {
    private final MapService mapService;
    private final MapMapper mapMapper;
    @Operation(summary = "위치 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2101", description = "사용중인 유저 닉네임 입니다.", content = @Content),
            @ApiResponse(responseCode = "2103", description = "존재하지 않는 유저입니다.", content = @Content),
            @ApiResponse(responseCode = "4001", description = "서버 오류입니다.", content = @Content)
    })
    @PostMapping("")
    public ResponseEntity<BaseResponse<String>> postMapPoint(@RequestBody PostMapPointReq postMapPointReq) {
        try {
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            long memberId = Long.parseLong(loggedInUser.getName());
            mapService.postMapPoint(mapMapper.toPostMapPointDto(memberId,postMapPointReq));
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(BaseResponseStatus.SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(BaseResponseStatus.SERVER_ERROR));
        }
    }
    @Operation(summary = "위치 받기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "2101", description = "사용중인 유저 닉네임 입니다.", content = @Content),
            @ApiResponse(responseCode = "2103", description = "존재하지 않는 유저입니다.", content = @Content),
            @ApiResponse(responseCode = "4001", description = "서버 오류입니다.", content = @Content)
    })
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<GetMapPointDto>>> getMapPoints(@RequestParam Long postId) {
        try {
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            long memberId = Long.parseLong(loggedInUser.getName());
            List<GetMapPointDto> getMapPointDtoList = mapService.getMapPointList(postId);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(getMapPointDtoList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(BaseResponseStatus.SERVER_ERROR));
        }
    }
}