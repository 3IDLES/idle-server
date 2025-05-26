# 👵🏻 케어밋 - 쉽고 빠르게 만나는 집 근처 요양 일자리 

<img width="900" alt="ppt_표지" src="https://github.com/user-attachments/assets/a022bac8-ad7e-4948-a796-7b1522c617dd">

<table>
  <tr>
    <td align="center"><img width="200" alt="1" src="https://github.com/user-attachments/assets/1453d60b-7bd4-4d47-8dde-65bc3e2e040d"></td>
    <td align="center"><img width="200" alt="2" src="https://github.com/user-attachments/assets/00ba937b-2cb9-4be9-af52-d510eb312fa2"></td>
    <td align="center"><img width="200" alt="3" src="https://github.com/user-attachments/assets/e0a3344f-20c0-4333-8409-5863e348533c"></td>
    <td align="center"><img width="200" alt="4" src="https://github.com/user-attachments/assets/1b9a8932-7a66-42c6-a5dd-cb486b3e134c"></td>
  </tr>
    <tr>
    <td align="center"><img width="200" alt="5" src="https://github.com/user-attachments/assets/e0bab4ac-cf67-4c0a-a040-ed4660cdef43"></td>
    <td align="center"><img width="200" alt="6" src="https://github.com/user-attachments/assets/6f33b21c-0adc-4052-b087-f4172fcacf5c"></td>
    <td align="center"><img width="200" alt="7" src="https://github.com/user-attachments/assets/48ea52fe-2655-4b39-a183-4fd0b34043df"></td>
    <td align="center"><img width="200" alt="8" src="https://github.com/user-attachments/assets/8b27518f-9b7f-4c15-abac-2c5004e64c26"></td>
  </tr>
</table>

<br>
<br>
<br>

# 🧾 서비스 설명

- 본 서비스는 **요양보호사 구직 시장의 특징을 분석**하여,  
  요양 센터와 요양보호사 간의 **구인·구직 과정을 보다 간편하게 지원**하는 애플리케이션입니다.

- **워크넷에 매일 등록되는 요양보호사 구인 공고와 위도 경도 기반위치 정보를 자동 크롤링**하여 수집하고,  
  이를 사용자에게 필요한 정보만으로 정재하여 **위치 정보를 기반으로 가까운 공고**를 제공합니다.

- 요양보호사는 관심 있는 공고에 대해 **센터에 전화하거나, 채팅 기능을 통해 직접 문의**할 수 있습니다.

> 👉 불필요한 탐색 없이, **위치 기반으로 실시간 맞춤 공고**를 확인하고,  
> **즉시 소통 가능한 채널**을 통해 효율적인 구직 활동을 지원합니다.

<br>
<br>
<br>

# 🛠️ Server Tech Stack

| 항목 | 내용 |
|------|------|
| 📆 진행 기간 | 2025.01 ~ 운영 중 |
| 🤖 백엔드 기술 스택 | Kotlin, Spring Boot, Spring Batch, Spring Data JPA, MySQL, Redis, S3 |
| ⚙️ 인프라 기술 스택 | Docker, GitHub Actions, Firebase Cloud Messaging, Sentry, Nginx, <br>AWS (VPC, Internet Gateway, NAT Gateway, Route Table, Security Group, EC2, RDS 등)<br> → 이후 비용 문제로 **Home Server 환경으로 이전** |

<br>
<br>
<br>

# 🏠 홈 서버 구조
<p align="center">
  <img src="https://github.com/user-attachments/assets/6443ceab-3ec4-4b3c-b62c-09b696fcbec9" alt="홈 서버 구조 다이어그램" />
</p>

🖥️ **고정 IP 및 외부 접근 설정**  
Ubuntu가 설치된 노트북에서 DHCP 설정을 비활성화하여 내부 IP를 고정하고,  
공유기에서 포트 포워딩을 통해 외부에서 접속 가능한 홈 서버 환경을 구축하였습니다.<br>

🌐 **Dynamic DNS를 통한 도메인 연결**  
공인 IP가 주기적으로 변경되는 문제를 해결하기 위해,  
Dynamic DNS 서비스를 사용하여 IP 변경 시에도 도메인으로 서버에 안정적으로 접근할 수 있도록 구성했습니다.<br>

🔐 **보안 설정 및 SSL 처리**  
- SSH 접속은 RSA 키 기반 인증으로 보안을 강화하였습니다.  
- 443 포트로 들어오는 HTTPS 요청에 대해서는 Nginx에서 SSL Termination을 적용해 암호화를 처리하였습니다.<br>

🔁 **Nginx 리버스 프록시 구성**  
Nginx에서 `/caremeet-dev`, `/caremeet` 등의 경로에 따라  
각기 다른 포트에서 실행 중인 개발용 및 운영용 애플리케이션 서버로 트래픽을 분기하였습니다.
<br>
<br>
<br>

# 애플리케이션 구조

<p align="center">
  <img src="https://github.com/user-attachments/assets/c004d2c9-5544-45d9-8977-82383f8c6744" alt="애플리케이션 구조 다이어그램" />
</p>


✅ **리버스 프록시 및 보안 구성**  
Nginx를 통해 외부 요청을 수신하고, URL 경로(`/caremeet-dev`, `/caremeet`)에 따라 트래픽을 개발 환경과 운영 환경으로 분기하도록 리버스 프록시를 설정했습니다. 또한, HTTPS 및 WebSocket Secure(wss)에 대해 SSL 인증서를 적용하여 보안 통신을 지원하였습니다.<br>
<br><br>
🔒 **내부 리소스 보안 강화**  
MySQL, Redis 등 내부 리소스는 외부에서 직접 접근할 수 없도록 구성하고, SSH 터널링을 통해서만 접근 가능하게 설정하여 네트워크 보안성을 높였습니다.<br>
<br><br>
⚙️ **CI/CD 자동화 구성**  
GitHub Actions를 활용해 CI/CD 파이프라인을 자동화하였고, 빌드된 이미지는 AWS ECR (Elastic Container Registry) 에 저장된 후 배포되었습니다.<br>
<br><br>
📡 **Redis 활용**

Redis는 단순 캐시를 넘어, 실시간 처리와 데이터 조회 최적화를 위한 핵심 컴포넌트로 다음과 같이 활용되었습니다:

- **채팅 Pub/Sub 메시지 브로커 및 세션 저장소**  
  Redis의 Pub/Sub 구조를 통해 채팅 메시지를 실시간으로 중계하고,
  채팅 중인 유저 세션 정보를 저장하여  알림 전송 시 수신 대상 필터링이 가능하도록 구현하였습니다.

- **읽음 처리 Write 최소화 전략**  
  Redis에는 채팅방별 최신 메시지 시퀀스와 사용자별 마지막 읽은 시퀀스를 저장하였고,  
  이를 기반으로 잦은 업데이트 쿼리( MySQL의 is_read 컬럼 업데이트)를 제거하였습니다.

- **채팅방 팅방 목록 조회 최적화 (JOIN 제거)**
  Redis에 저장된 채팅방별 메시지 시퀀스 정보와 MySQL의 채팅방 메타데이터를 조합하여
  복잡한 JOIN 없이 채팅방 목록, 안읽은 메시지 개수 및상태를 효율적으로 조회할 수 있도록 구성하였습니다.
  이를 통해 슬로우 쿼리를 유발하던 JOIN 연산을 제거하고, 응답 속도를 개선하였습니다.

- **위치 기반 공고 조회**  
  기존의 ST_Buffer 기반 MySQL 공간 쿼리를 제거하고, Redis의 GEO 자료구조를 활용하여
  사용자의 현재 위치 기준 반경 내 공고 조회 성능을 크게 향상시켰습니다.

<br><br>
🗄️ **MySQL 활용**

MySQL은 핵심 비즈니스 데이터를 안정적으로 관리하고,  
Redis와의 조합을 통해 실시간성과 정합성을 모두 충족시키는 기반 저장소로 활용되었습니다.

- **핵심 비즈니스 데이터 저장소**  
  사용자, 센터, 센터장, 채팅방, 채팅 메시지 등  
  주요 엔터티를 MySQL에 저장하여 데이터 정합성과 일관성을 유지하였습니다.

- **최적화된 조회 쿼리 설계**  
  - 카디널리티를 고려한 인덱스 설계 및 실행 계획(EXPLAIN) 분석을 통해 주요 조회 성능을 개선하였습니다.  
  - 채팅방 목록 조회 시, LATERAL JOIN을 활용하여  
    각 채팅방의 최신 메시지를 효율적으로 가져오는 쿼리 구조를 구성하였습니다.  
  - Time-based UUID를 직접 생성하여 기본 키로 사용함으로써  
    - 추후 Replication 환경에서도 안정적인 Row 기반 복제를 지원할 수 있도록 두고,
    - ID 조회를 위한 불필요한 쿼리를 제거하였습니다.

- **데이터 무결성과 동시성 제어**  
  - 애플리케이션 레벨에서의 중복 요청 및 경쟁 조건을 방지하기 위해  
    중복 가능한 필드에 Unique 인덱스를 설정하여 동시성 문제를 예방하였습니다.

- **불필요한 데이터 정리 자동화**  
  - 2주 이상 경과한 크롤링 공고 데이터는 자동으로 삭제되도록  
    스케줄러 + MySQL 이벤트 프로시저(Event + Stored Procedure)를 통해  
    데이터를 주기적으로 정리하는 자동화 로직을 구현하였습니다.
  
<br>
<br>
<br>



# Features

### 👉 요양 보호사 기능

<table>
  <tr>
    <td align="center"><b>인 앱공고</b></td>
    <td align="center"><b>워크넷공고</b></td>
    <td align="center"><b>프로필</b></td>
  </tr>
    <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/dddade49-d61b-4c68-be1d-4e1365499158" width=200/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/106cb654-44ff-4348-9f36-c8b2cb431037" width=200/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/aac9bf44-5466-4f48-b115-81a18e571b73" width=200/></td>
  </tr>
</table>
<table>
  <tr>
    <td align="center"><b>회원탈퇴</b></td>
    <td align="center"><b>카톡공유</b></td>
    <td align="center"><b>딥링크/지연된 딥링크</b></td>
  </tr>
    <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/99b990f8-6da7-4268-aee3-344b8fa97fca" width=200/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/cd42347c-7197-48a6-9e88-17a66e0eb6dd" width=200/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/ead52310-091f-4826-9102-1c2e8b5b0e53" width=200/></td>
  </tr>
</table>
<br><br>

### 👉 센터 관리자 기능

<table>
  <tr>
    <td align="center"><b>회원가입 1</b></td>
    <td align="center"><b>회원가입 2</b></td>
    <td align="center"><b>로그인 및 인증대기</b></td>
  </tr>
    <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/3f923e51-3edd-443b-8830-4876eda1d635" width=200/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/898007c0-fac2-436c-8834-e5697b348187" width=200/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/1f46c2be-debd-4112-a0f2-5550da189653" width=200/></td>
  </tr>
</table>
<table>
  <tr>
    <td align="center"><b>신규 비밀번호 발급</b></td>
    <td align="center"><b>공고 등록 1</b></td>
    <td align="center"><b>공고 등록 2</b></td>
  </tr>
    <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/9db38630-b7c4-4aa8-a0e5-aa90a2c3aa0f" width=200/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/0a467a15-be3f-4e13-9291-454303893558" width=200/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/f5d9003a-7027-4931-aec8-8af8641421e4" width=200/></td>
  </tr>
</table>
<table>
  <tr>
    <td align="center"><b>지원자 확인</b></td>
  </tr>
    <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/228bda84-1d09-40b2-b990-55725f17b148" width=200/></td>
  </tr>
</table>
<br><br>

### 👉 공통 기능

<table>
  <tr>
    <td align="center"><b>FCM</b></td>
    <td align="center"><b>알림 센터</b></td>
  </tr>
    <tr>
    <td align="center"><img src="https://github.com/user-attachments/assets/10330ca2-e33c-4957-84c8-03ae8763f63f" width=200/></td>
    <td align="center"><img src="https://github.com/user-attachments/assets/4eb5cd91-706d-413c-92ca-3310df2d10e2" width=200/></td>
  </tr>
</table>

<br><br><br>
# 👍 서비스 성과

<img width="900" alt="image" src="https://github.com/user-attachments/assets/5bdd9988-f197-4482-82df-9a45fbb9062a">

<br><br>

<img width="900" alt="image" src="https://github.com/user-attachments/assets/8ce335f2-e890-4ee0-ac7b-40a13cf20ad5">
