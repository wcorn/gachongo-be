package gcu.mp.api.orderPost.mapper;

import gcu.mp.api.orderPost.dto.request.CreateOrderPostReq;
import gcu.mp.service.orderPost.dto.CreateOrderPostDto;
import org.springframework.stereotype.Component;

@Component
public class OrderPostMapper {
    public CreateOrderPostDto toCreateOrderPostDto(Long memberId, CreateOrderPostReq createOrderPostReq){
        return CreateOrderPostDto.builder()
                .estimatedTime(createOrderPostReq.getEstimatedTime())
                .title(createOrderPostReq.getTitle())
                .content(createOrderPostReq.getContent())
                .memberId(memberId).build();
    }
}
