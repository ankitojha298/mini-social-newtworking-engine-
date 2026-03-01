# ConnectHub: Mini Social Network Engine

## About The Project

This project is a fully functional Mini Social Network built using an advanced Java + React stack, strategically designed to utilize and demonstrate **6 Core Data Structures** mapping to real-world social networking logic.

### 🔌 Interactive Features Built
- **Account Creation & JWT Authentication**
- **User Search Autocomplete** powered by a custom Prefix Trie (`UserTrie`) giving $O(m)$ search complexity.
- **Mutual Friend Graph Mapping** powered by BFS running through an Adjacency List (`SocialGraph`).
- **Dynamic News Feed Timeline** powered by $O(1)$ Prepends to a custom Doubly-Linked-List (`NewsFeedList`).
- **Real-Time Websocket Notifications** queued and delivered via Thread-Safe FIFO operations (`NotificationQueue`).
- **LIFO Profile Navigation Stack** giving history-back "Undo" capabilities (`NavigationStack`).
- **Real-Time Trending Index** parsing hashtags via mapped Fixed Size Arrays (`TrendingArray`).

### ⚙ Technology Stack
- **Frontend Layer:** React JS, Vite, Axios
- **Backend Layer:** Java 17, Spring Boot 3, Spring WebSockets, Spring Data JPA, JWT Headers
- **Data Layer:** H2 In-Memory Database (Fallback from PostgreSQL via Docker)

---

## Running Locally

Because Docker / WSL was unavailable on the host machine during compilation, the project was hot-swapped to use an **In-Memory Embedded Server (H2 Database)**. This makes the project 100% portable with zero database setup required!

### 1) Start the Backend API
Navigate into the backend directory and trigger the embedded Maven wrapper:
```bash
cd backend
./apache-maven-3.9.6/bin/mvn.cmd clean spring-boot:run
```

### 2) Start the Frontend Dashboard
Navigate into the frontend directory and start Vite:
```bash
cd frontend
npm install
npm run dev
```

Open a browser to `http://localhost:5173/` and sign up to start interacting with the custom data structures!
