package gcu.mp.service.orderPost;

import gcu.mp.common.api.BaseResponseStatus;
import gcu.mp.common.exception.BaseException;
import gcu.mp.domain.entity.BaseEntity;
import gcu.mp.domain.member.domin.Member;
import gcu.mp.domain.orderPost.domain.OrderPost;
import gcu.mp.domain.orderPost.domain.OrderPostComment;
import gcu.mp.domain.orderPost.domain.OrderPostProgress;
import gcu.mp.domain.orderPost.repository.OrderPostCommentRepository;
import gcu.mp.domain.orderPost.repository.OrderPostProgressRepository;
import gcu.mp.domain.orderPost.repository.OrderPostRepository;
import gcu.mp.domain.orderPost.vo.ProgressState;
import gcu.mp.domain.orderPost.vo.State;
import gcu.mp.service.member.MemberService;
import gcu.mp.service.orderPost.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static gcu.mp.common.api.BaseResponseStatus.NOT_EXIST_COMMENT;
import static gcu.mp.common.api.BaseResponseStatus.NOT_EXIST_POST;
import static gcu.mp.domain.orderPost.vo.Progress.ING;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderPostServiceImpl implements OrderPostService {
    private final MemberService memberService;
    private final OrderPostRepository orderPostRepository;
    private final OrderPostCommentRepository orderPostCommentRepository;
    private final OrderPostProgressRepository orderPostProgressRepository;

    @Override
    @Transactional
    public void createOrderPost(CreateOrderPostDto createOrderPostDto) {
        Member member = memberService.getMember(createOrderPostDto.getMemberId());
        OrderPost orderPost = OrderPost.builder()
                .content(createOrderPostDto.getContent())
                .title(createOrderPostDto.getTitle())
                .estimated_time(createOrderPostDto.getEstimatedTime())
                .state(State.A)
                .progress(ING)
                .build();
        orderPost.setMember(member);
        orderPostRepository.save(orderPost);
    }

    //todo 댓글 수 가져오기
    @Override
    public List<GetOrderPostListDto> getOrderPostList(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        List<OrderPost> orderPostList = orderPostRepository.findByState(State.A, pageRequest);
        return orderPostList.stream().map(
                orderPost -> GetOrderPostListDto.builder()
                        .orderId(orderPost.getId())
                        .estimatedTime(orderPost.getEstimated_time())
                        .progress(orderPost.getProgress().getName())
                        .commentNum(0)
                        .title(orderPost.getTitle()).build()
        ).collect(Collectors.toList());
    }

    @Override
    public GetOrderPostDetailDto getOrderPostDetail(Long orderPostId) {
        OrderPost orderPost = getOrderPost(orderPostId);
        return GetOrderPostDetailDto.builder()
                .writer(orderPost.getMember().getNickname())
                .commentNum(0)
                .content(orderPost.getContent())
                .estimatedTime(orderPost.getEstimated_time())
                .title(orderPost.getTitle()).build();
    }

    @Override
    public List<OrderPostCommentDto> getOrderPostCommentList(Long orderPostId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        List<OrderPostComment> orderPostCommentList = orderPostCommentRepository.findByOrderPostIdAndState(orderPostId, State.A, pageRequest);
        return orderPostCommentList.stream().map(
                orderPostComment -> OrderPostCommentDto.builder()
                        .commentId(orderPostComment.getId())
                        .commentWriter(orderPostComment.getMember().getNickname())
                        .content(orderPostComment.getContent()).build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createOrderPostDetailComment(CreateOrderPostCommentDto createOrderPostCommentDto) {
        Member member = memberService.getMember(createOrderPostCommentDto.getMemberId());
        OrderPost orderPost = getOrderPost(createOrderPostCommentDto.getOrderPostId());
        OrderPostComment orderPostComment = OrderPostComment.builder()
                .content(createOrderPostCommentDto.getContent())
                .state(State.A).build();
        orderPostComment.setOrderPost(orderPost);
        orderPostComment.setMember(member);
        orderPostCommentRepository.save(orderPostComment);
    }

    @Override
    @Transactional
    public void selectOrderPostCustomer(Long orderPostId, Long commentId) {
        OrderPost orderPost = getOrderPost(orderPostId);
        Member member = getOrderPostComment(commentId).getMember();
        OrderPostProgress orderPostProgress = OrderPostProgress.builder()
                .state(State.A)
                .progressState(ProgressState.ING).build();
        orderPostProgress.setOrderPost(orderPost);
        orderPostProgress.setMember(member);
        orderPostProgressRepository.save(orderPostProgress);
    }

    @Override
    public List<Member> getOrderPostProgressMemberIdByMemberId(Long memberId) {
        Optional<OrderPostProgress> orderPostProgressOptional = orderPostProgressRepository.findByMemberIdAndStateAndProgressState(memberId,State.A,ProgressState.ING);
        if(orderPostProgressOptional.isEmpty())
            return new ArrayList<>();
        else {
            Long orderPostId = orderPostProgressOptional.get().getId();
            List<OrderPostProgress> orderPostProgressList = orderPostProgressRepository.findByOrderPostIdAndStateAndProgressState(orderPostId,State.A,ProgressState.ING);
            return orderPostProgressList.stream().map(
                    OrderPostProgress::getMember
            ).collect(Collectors.toList());
        }
    }

    @Override
    public Long getOrderPostProgressOrderIdByMemberId(Long memberId) {
        Optional<OrderPostProgress> orderPostProgressOptional = orderPostProgressRepository.findByMemberIdAndStateAndProgressState(memberId,State.A,ProgressState.ING);
        return orderPostProgressOptional.map(BaseEntity::getId).orElse(null);
    }

    public OrderPostComment getOrderPostComment(Long commentId){
        return orderPostCommentRepository.findByIdAndState(commentId, State.A).orElseThrow(() -> new BaseException(NOT_EXIST_COMMENT));
    }
    public OrderPost getOrderPost(Long orderPostId){
        return orderPostRepository.findByIdAndState(orderPostId, State.A).orElseThrow(() -> new BaseException(NOT_EXIST_POST));
    }
}