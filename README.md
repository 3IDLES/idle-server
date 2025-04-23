#  케어밋 - 쉽고 빠르게 만나는 집 근처 요양 일자리 

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

# 서비스 설명

- 요양보호사 구직 사이트의 특징을 분석하여, 센터와 요양보호사 간 구인·구직 프로세스를 보다 편리하게 지원하는 애플리케이션입니다.
- 워크넷에 매일 업데이트되는 공지를 크롤링하여, 사용자의 위치를 기반으로 가까운 순서대로 센터 구인 공고를 제공합니다. 이를 통해 보호사는 구직을 위해 센터에 전화하거나 채팅으로 직접 문의할 수 있습니다.

<br>
<br>
<br>

#  Server Tech Stack

| 📆 진행 기간 | 2025.01 ~ 운영 중 |
|-------------|------------------|
| 🤖 백엔드 사용 기술 | Kotlin, Spring Boot, Spring Batch, Data JPA, MySQL, Redis |
| ⚙️ 인프라 사용 기술 | Docker, GitHub Actions, Firebase Cloud Message, Sentry,<br> AWS (VPC, Internet Gateway, NAT Gateway, Route Table, Security Group, EC2, RDS 등) |

<br>
<br>
<br>

# AWS 클라우드 구조

![image](https://github.com/user-attachments/assets/e8af5059-55c1-485e-b330-283a3c729d10)


- VPC 기반의 클라우드 인프라를 구성하여, 퍼블릭/프라이빗 서브넷, Bastion Host, NAT Gateway, ALB 등을 통해 보안성과 가용성을 확보했습니다.
- 또한, 개발/운영 환경을 분리하고, RDS와 ElastiCache 등을 활용해 안정적인 서비스 운영이 가능한 구조를 설계했습니다.

<br>
<br>
<br>

# 애플리케이션 구조

![image](https://github.com/user-attachments/assets/859c3093-3e8b-4fd4-839f-aec189cd32e4)

- ALB를 통해 외부 요청을 수신하고, Dev/Prod 환경에 따라 트래픽을 분산시켜 안정성과 확장성을 확보했습니다.
- Bastion Server를 통해서만 내부 자원(MySQL, Redis 등)에 접근 가능하도록 설정하여 보안성을 강화했습니다.
- GitHub와 GitAction을 연동해 CI/CD 자동화를 구현하고, Bastion을 통해 서버에 배포하도록 구성했습니다.
- Redis는 Session Storage 용도외에도 Pub/Sub 모델을 적용해 채팅메시지 전송에 이벤트 처리로 활용했습니다.

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
