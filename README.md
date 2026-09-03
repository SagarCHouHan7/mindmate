# MindMate - AI-Powered Mental Wellness Platform

## 🌟 About the Project

MindMate is an advanced mental wellness platform that leverages agentic AI to provide intelligent, empathetic, and continuous emotional support. The system integrates multiple specialized AI agents to simulate real-world mental health assistance, offering users a comprehensive digital mental health companion.

## 🎯 Problem Statement

Current mental health solutions face significant challenges:

**Accessibility Issues:**
- Limited availability of mental health professionals
- High costs of therapy and counseling
- Geographic barriers to accessing care
- Stigma associated with seeking help

**Technical Limitations:**
- Lack of instant and continuous support
- Absence of context-aware conversations
- Missing early risk detection systems
- Unsafe or unmoderated online environments

**Consequences:**
- Delayed help-seeking behavior
- Poor mental health outcomes
- Lack of intervention at critical moments
- Inadequate support for chronic conditions

## 🏗️ System Architecture

MindMate implements a sophisticated multi-agent architecture where different AI agents specialize in specific aspects of mental health support:

### Core AI Agents

#### 1. **Wellness Conversation Agent**
- **Location:** `com.MindMate.agents.wellness.Service.ChatService`
- **Purpose:** Generates empathetic, context-aware responses to user messages
- **Features:**
  - Streaming responses for real-time interaction
  - Integration with memory systems for context awareness
  - Personalized responses based on user profile and history
  - Redis-based chat history for performance optimization

#### 2. **Risk Detection Agent**
- **Location:** `com.MindMate.agents.escalation.RiskDetectionService`
- **Purpose:** Monitors user conversations for signs of mental health crises
- **Features:**
  - Real-time risk level assessment (SEVERE, HIGH, MODERATE, LOW, UNKNOWN)
  - Keyword-based immediate detection for severe risks
  - Context-aware risk evaluation using conversation history
  - Automated escalation protocols for high-risk situations
  - Periodic risk assessment based on conversation patterns

#### 3. **Memory Management Agent**
- **Location:** `com.MindMate.agents.untils.MemoryService`
- **Purpose:** Maintains long-term user context and memory
- **Features:**
  - Dynamic summary updates using AI
  - Long-term memory storage and retrieval
  - Integration with RAG for semantic search
  - Context preservation across sessions

#### 4. **RAG (Retrieval-Augmented Generation) Agent**
- **Location:** `com.MindMate.agents.wellness.Service.RagService`
- **Purpose:** Enhances AI responses with relevant past memories
- **Features:**
  - Vector-based semantic search using Qdrant
  - Contextual memory retrieval
  - Integration with conversation history
  - Enhanced response personalization

#### 5. **Moderation Agent**
- **Location:** `com.MindMate.agents.moderation.ModerationService`
- **Purpose:** Ensures safe community interactions
- **Features:**
  - AI-powered content moderation
  - Safety rating system (1-10 scale)
  - Automatic content filtering
  - Suggested safer alternatives for borderline content
  - Real-time policy violation detection

#### 6. **Assessment Agent**
- **Location:** `com.MindMate.agents.assessment.service.AssessmentService`
- **Purpose:** Administers and analyzes mental health assessments
- **Features:**
  - Multiple assessment types (PHQ-9, GAD-7, etc.)
  - AI-powered result interpretation
  - Personalized recommendations based on scores
  - Eligibility tracking and timing control
  - Async processing for user experience

#### 7. **Care Journey Agent**
- **Location:** `com.MindMate.agents.carejourney.CareJourneyAgentService`
- **Purpose:** Generates comprehensive patient reports for experts
- **Features:**
  - Integration with appointment system
  - Comprehensive patient context compilation
  - AI-generated report generation
  - Assessment result integration
  - Expert notification system

#### 8. **Quick Chat Agent**
- **Location:** `com.MindMate.agents.quickchat.QuickChatService`
- **Purpose:** Provides instant, lightweight support
- **Features:**
  - Rapid response generation
  - Reduced context requirements
  - Immediate support for urgent needs

## 💻 Technology Stack

### Backend Framework
- **Spring Boot 4.0.6** - Core application framework
- **Java 21** - Programming language
- **Spring AI 2.0.0-M4** - AI integration framework

### AI & Machine Learning
- **OpenAI GPT** - Primary AI model for conversations
- **Spring AI** - AI integration and orchestration
- **Qdrant Vector Database** - RAG implementation and semantic search

### Database & Storage
- **MySQL 8.4.0** - Primary relational database
- **Redis** - Chat history caching and session management
- **Qdrant** - Vector database for RAG implementation

### Security & Authentication
- **Spring Security** - Authentication and authorization
- **JWT (jjwt 0.11.5)** - Token-based authentication
- **BCrypt** - Password encryption
- **Role-based Access Control** - USER, EXPERT, ADMIN roles

### Additional Services
- **Razorpay** - Payment processing for appointments
- **Spring Mail** - Email notifications and communication
- **Bucket4j** - Rate limiting and API protection
- **Swagger/OpenAPI** - API documentation

### Development Tools
- **Lombok** - Code generation and boilerplate reduction
- **Maven** - Build and dependency management
- **Spring DevTools** - Development productivity tools

## 🚀 Key Features

### Multi-Agent AI System
- **Specialized Agents:** Each agent handles specific mental health aspects
- **Agent Orchestration:** Seamless coordination between agents
- **Context Sharing:** Agents share user context and memory
- **Async Processing:** Non-blocking AI operations for better UX

### Advanced Memory System
- **Long-term Memory:** Persistent user summaries and context
- **Short-term Memory:** Recent conversation history
- **RAG Integration:** Semantic search for relevant past interactions
- **Redis Caching:** High-performance chat history access

### Risk Detection & Escalation
- **Real-time Monitoring:** Continuous risk assessment
- **Multi-level Detection:** Keyword + AI-based risk detection
- **Automated Escalation:** Protocols for different risk levels
- **Historical Tracking:** Risk status evolution over time

### Community Support with AI Moderation
- **Safe Environment:** AI-powered content moderation
- **Policy Enforcement:** Automated community guidelines
- **Graded Response:** Warning, modification, or removal based on severity
- **User Feedback:** Clear notification system

### Assessment & Analytics
- **Standardized Assessments:** PHQ-9, GAD-7, and custom assessments
- **AI Interpretation:** Contextual result analysis
- **Personalized Recommendations:** Tailored suggestions based on results
- **Eligibility Control:** Time-based assessment availability

### Professional Integration
- **Expert Verification:** Document-based expert verification
- **Appointment Booking:** Scheduling with mental health professionals
- **Payment Processing:** Secure payment integration
- **Care Reports:** AI-generated patient summaries for experts

### Security & Performance
- **JWT Authentication:** Secure token-based auth
- **Rate Limiting:** API protection and abuse prevention
- **Role-based Access:** Granular permission control
- **Async Processing:** Non-blocking operations for scalability

## 📁 Project Structure

```
com.MindMate/
├── agents/                    # AI Agent implementations
│   ├── assessment/           # Mental health assessments
│   ├── carejourney/          # Patient care coordination
│   ├── escalation/           # Risk detection and escalation
│   ├── lifestyle/            # Lifestyle and routine analysis
│   ├── moderation/           # Content moderation
│   ├── quickchat/            # Quick support conversations
│   ├── untils/               # Memory and chat utilities
│   └── wellness/             # Main wellness conversations
├── appointments/             # Appointment booking system
├── community/                # Community forum and Q&A
├── config/                   # Application configuration
├── controller/               # REST API controllers
├── dto/                      # Data Transfer Objects
├── exception/                # Exception handling
├── model/                    # Database entities
├── repository/               # Database repositories
├── security/                 # Security configuration
└── service/                  # Business logic services
```

## 🔌 API Endpoints

### Public Endpoints
- `POST /public/register` - User registration
- `POST /public/login` - User authentication
- `GET /public/tips` - Wellness tips and resources

### User Endpoints
- `POST /user/chat` - Send message to AI wellness agent
- `GET /user/chat/history` - Retrieve conversation history
- `POST /api/assessment/submit` - Submit mental health assessment
- `GET /api/assessment/questions` - Get assessment questions
- `POST /appointment/user/book` - Book appointment with expert

### Expert Endpoints
- `GET /expert/profile` - Get expert profile
- `POST /expert/verify` - Submit verification documents
- `GET /appointment/expert/schedule` - View appointment schedule
- `POST /api/media/upload` - Upload media content

### Community Endpoints
- `POST /api/question/create` - Create community question
- `POST /api/answer/create` - Answer community questions
- `GET /api/question/all` - Browse community questions

### Admin Endpoints
- `GET /admin/experts/pending` - View pending expert verifications
- `POST /admin/experts/approve` - Approve expert verification

## 🔐 Security Architecture

### Authentication Flow
1. User registers/login via `/public/*` endpoints
2. Server generates JWT token upon successful authentication
3. Client includes JWT in Authorization header for subsequent requests
4. `JwtAuthFilter` validates token on each protected request
5. `AccountDetailsService` loads user details for authorization

### Authorization Levels
- **ADMIN:** Full system access, expert verification, platform management
- **EXPERT:** Professional features, appointment management, community participation
- **USER:** Basic wellness features, community access, appointment booking

### Rate Limiting
- Implemented using Bucket4j
- Prevents API abuse and ensures fair usage
- Configurable per endpoint and user role

## 🗄️ Database Schema

### Core Entities
- **Account:** Base user entity with authentication data
- **User:** Extended user profile with wellness-specific data
- **Expert:** Professional profile with verification and credentials
- **Admin:** System administrator account

### Conversation & Memory
- **ChatMessage:** Individual conversation messages
- **UserMemory:** Long-term user context and summaries
- **RiskStatus:** User risk level tracking

### Assessment & Care
- **Assessment:** Mental health assessment definitions
- **AssessmentQuestion:** Individual assessment questions
- **AssessmentResult:** User assessment responses and scores
- **PatientReport:** AI-generated care journey reports

### Community
- **Question:** Community forum questions
- **Answer:** Community question answers with moderation data

### Appointments
- **Appointment:** Booking details with payment status

## 🧠 AI Integration Details

### Prompt Engineering
The system uses structured prompts located in `src/main/resources/prompts/`:

- **AIWellnessExpert/chat-guide.st** - Main conversation agent instructions
- **AIWellnessExpert/risk-detection-template.st** - Risk assessment guidelines
- **AIWellnessExpert/user-memory-update-template.st** - Memory update instructions
- **community-support-policy-system-check.st** - Moderation guidelines
- **care-journey-report-template.st** - Report generation template
- **assessment-recommendation-template.st** - Assessment recommendation logic

### Memory Architecture
1. **Immediate Context:** Last 10 messages from Redis cache
2. **Long-term Summary:** AI-maintained user profile summary
3. **RAG Retrieval:** Semantic search of past relevant conversations
4. **Risk Context:** Current risk status and history

### Configuration
AI configuration in `application.properties`:
```properties
spring.ai.openai.api-key=your-openai-key
spring.ai.vectorstore.qdrant.host=localhost
spring.ai.vectorstore.qdrant.port=6334
spring.ai.vectorstore.qdrant.collection-name=mindmate-memory
```

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- MySQL 8.0+
- Redis server
- Qdrant vector database
- OpenAI API key

### Installation

1. **Clone the repository**
```bash
git clone <repository-url>
cd mindmate
```

2. **Configure databases**
- Set up MySQL database named `mindmate`
- Configure Redis server
- Set up Qdrant instance

3. **Configure application properties**
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mindmate
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.ai.openai.api-key=your-openai-key
```

4. **Build the project**
```bash
./mvnw clean install
```

5. **Run the application**
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### API Documentation
Once running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## 🔧 Configuration

### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mindmate
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

### Email Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### Payment Configuration
```properties
razorpay.key.id=your-razorpay-key-id
razorpay.key.secret=your-razorpay-secret
```

## 🧪 Testing

The project includes comprehensive test coverage:
- Unit tests for services and business logic
- Integration tests for API endpoints
- Security tests for authentication and authorization

Run tests with:
```bash
./mvnw test
```

## 🌐 Deployment Considerations

### Production Checklist
- [ ] Replace hardcoded API keys with environment variables
- [ ] Configure production database connection pooling
- [ ] Set up proper Redis clustering for scalability
- [ ] Configure Qdrant for production use
- [ ] Enable HTTPS/TLS for all endpoints
- [ ] Set up monitoring and logging
- [ ] Configure backup strategies for databases
- [ ] Implement proper error handling and recovery
- [ ] Set up CI/CD pipeline
- [ ] Configure rate limiting for production loads

### Environment Variables
Key environment variables for production:
```bash
OPENAI_API_KEY=your-production-key
DB_USERNAME=prod-user
DB_PASSWORD=secure-password
REDIS_HOST=redis-cluster
QDRANT_HOST=qdrant-production
RAZORPAY_KEY_ID=production-key
RAZORPAY_KEY_SECRET=production-secret
```

## 📈 Future Enhancements

### Planned Features
- **Voice Analysis:** Integrate speech-to-text for voice-based emotional analysis
- **Wearable Integration:** Connect with fitness trackers for holistic health monitoring
- **Multilingual Support:** Expand AI capabilities for multiple languages
- **Advanced Analytics:** Comprehensive dashboard for mental health trends
- **Mobile Applications:** Native iOS and Android applications
- **Video Consultations:** Integration with video conferencing for remote therapy
- **Machine Learning Models:** Custom ML models for personalized predictions
- **Blockchain Integration:** Secure health records management

### Technical Improvements
- **Microservices Architecture:** Split into separate services for better scalability
- **GraphQL API:** Alternative to REST for more efficient data fetching
- **Real-time Communication:** WebSocket integration for live features
- **Advanced Caching:** Multi-layer caching strategy
- **Database Sharding:** Horizontal scaling for large user bases

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:
1. Fork the repository
2. Create a feature branch
3. Make your changes with proper testing
4. Submit a pull request with detailed description

## 📞 Contact

- **Developer:** Sagar Chouhan
- **Email:** sagarsinghchouhan1705@gmail.com
- **Phone:** +91 9893030231

## 📄 License

This project is developed for academic and innovation purposes. Please contact the developer for licensing information.

## 🙏 Acknowledgments

- Spring AI team for the excellent AI integration framework
- OpenAI for providing the GPT models
- Qdrant team for the vector database solution
- Spring Boot community for the robust framework

---

**Note:** This project handles sensitive mental health data. Ensure proper security measures, privacy compliance, and ethical considerations when deploying in production environments.