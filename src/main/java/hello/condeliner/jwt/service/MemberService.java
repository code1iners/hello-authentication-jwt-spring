package hello.condeliner.jwt.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hello.condeliner.jwt.dto.MemberDto;
import hello.condeliner.jwt.model.Member;
import hello.condeliner.jwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findOneWithAuthoritiesByUsername(username)
            .map(member -> createUser(username, member))
            .orElseThrow(() -> new UsernameNotFoundException(username + "를 찾을 수 없습니다."));
    }

    private User createUser(String username, Member member) {
        if (!member.isActivated()) {
            throw new RuntimeException(username + "는 활성화되지 않은 회원입니다.");
        }
        List<SimpleGrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
            .collect(Collectors.toList());
        return new User(member.getUsername(), member.getPassword(), grantedAuthorities);
    }
}
