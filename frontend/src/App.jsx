import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './index.css';

const api = axios.create({ baseURL: 'http://localhost:8080/api' });

export default function App() {
  const [user, setUser] = useState(null);
  const [isLoginView, setIsLoginView] = useState(true);

  if (!user) {
    return <AuthScreen onLogin={setUser} isLoginView={isLoginView} setIsLoginView={setIsLoginView} />;
  }

  return <Dashboard user={user} onLogout={() => setUser(null)} />;
}

function AuthScreen({ onLogin, isLoginView, setIsLoginView }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');

  const submit = async (e) => {
    e.preventDefault();
    if (isLoginView) {
      try {
        const res = await api.post('/auth/login', { username, password });
        api.defaults.headers.common['Authorization'] = `Bearer ${res.data.token}`;
        onLogin(res.data);
      } catch (e) {
        alert("Login failed");
      }
    } else {
      try {
        await api.post('/auth/register', { username, email, password });
        alert("Registration successful! You can now log in.");
        setIsLoginView(true);
      } catch (e) {
        alert(e.response?.data || "Registration failed");
      }
    }
  };

  return (
    <div style={{ display: 'flex', height: '100vh', alignItems: 'center', justifyContent: 'center' }}>
      <div className="panel animate-enter" style={{ width: '400px', display: 'flex', flexDirection: 'column', gap: '15px' }}>
        <h2 style={{ textAlign: 'center', margin: '0 0 10px 0' }}>ConnectHub ⚡</h2>
        <p style={{ textAlign: 'center', color: 'var(--text-muted)', margin: '-10px 0 10px 0' }}>
          Data Structure Powered Network
        </p>
        <form onSubmit={submit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
          <input placeholder="Username" required value={username} onChange={e => setUsername(e.target.value)} />
          {!isLoginView && <input placeholder="Email" required value={email} onChange={e => setEmail(e.target.value)} />}
          <input placeholder="Password" type="password" required value={password} onChange={e => setPassword(e.target.value)} />
          <button type="submit" style={{ padding: '12px' }}>{isLoginView ? 'Login to Network' : 'Create Account'}</button>
        </form>
        <p style={{ textAlign: 'center', fontSize: '0.9rem' }}>
          {isLoginView ? "Don't have an account? " : "Already have an account? "}
          <span style={{ color: 'var(--primary)', cursor: 'pointer', textDecoration: 'underline' }} onClick={() => setIsLoginView(!isLoginView)}>
            {isLoginView ? "Sign up" : "Log in"}
          </span>
        </p>
      </div>
    </div>
  );
}

function Dashboard({ user, onLogout }) {
  const [feed, setFeed] = useState([]);
  const [trends, setTrends] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [friends, setFriends] = useState([]);
  const [suggestions, setSuggestions] = useState([]);

  // Custom states
  const [searchQ, setSearchQ] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [navHistory, setNavHistory] = useState([]);
  const [currentProfile, setCurrentProfile] = useState(null);

  useEffect(() => {
    fetchFeed();
    fetchTrends();
    fetchFriends();
    fetchSuggestions();

    // WebSocket for O(1) Queue updates
    const ws = new WebSocket(`ws://localhost:8080/ws`);
    ws.onmessage = (event) => {
      setNotifications(prev => [event.data, ...prev]);
    };
    return () => ws.close();
  }, [currentProfile]);

  const fetchFeed = async () => setFeed((await api.get('/posts/feed')).data);
  const fetchTrends = async () => setTrends((await api.get('/trends')).data);
  const fetchFriends = async () => setFriends((await api.get(`/users/${user.id}/friends`)).data);
  const fetchSuggestions = async () => setSuggestions((await api.get(`/users/${user.id}/suggestions`)).data);

  // Search using Trie
  const handleSearch = async (val) => {
    setSearchQ(val);
    if (val.trim()) {
      const res = await api.get(`/users/search?q=${val}`);
      setSearchResults(res.data);
    } else {
      setSearchResults([]);
    }
  };

  // Nav Stack
  const visitProfile = async (targetUserId, targetUsername) => {
    await api.post(`/nav/visit/${targetUserId}`);
    setNavHistory(prev => [...prev, currentProfile || 'Home']);
    setCurrentProfile(targetUsername);
  };

  const goBack = async () => {
    const res = await api.post('/nav/back');
    if (res.data === "Stack is empty" || navHistory.length === 0) return;
    const last = navHistory[navHistory.length - 1];
    setNavHistory(prev => prev.slice(0, -1));
    setCurrentProfile(last === 'Home' ? null : last);
  };

  const addFriend = async (targetId) => {
    await api.post(`/users/${targetId}/friend`);
    alert("Friend added! (Graph Edge created)");
    fetchFriends();
    fetchSuggestions();
  };

  return (
    <div style={{ maxWidth: '1400px', margin: '0 auto', padding: '20px', display: 'grid', gridTemplateColumns: '1fr 2fr 1fr', gap: '20px' }}>

      {/* LEFT SIDEBAR: Array + Stack */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
        <div className="panel animate-enter">
          <h3 style={{ margin: '0 0 10px 0' }}>Profile History <span className="badge">LIFO Stack</span></h3>
          <p style={{ margin: '0 0 10px 0', fontSize: '0.95rem' }}>Viewing: <b style={{ color: 'var(--primary)' }}>{currentProfile || 'Global Feed'}</b></p>
          <div style={{ display: 'flex', gap: '10px' }}>
            <button onClick={goBack} disabled={navHistory.length === 0}>
              ◀ Pop Stack
            </button>
            {!currentProfile && <button onClick={() => visitProfile(999, 'Elon_Musk')} style={{ background: 'var(--border)' }}>Push Fake Profile</button>}
          </div>
          {navHistory.length > 0 && (
            <div style={{ marginTop: '15px', fontSize: '0.8rem', color: 'var(--text-muted)' }}>
              Stack Trace: [{navHistory.join(', ')}]
            </div>
          )}
        </div>

        <div className="panel animate-enter" style={{ animationDelay: '0.1s' }}>
          <h3 style={{ margin: '0 0 10px 0' }}>Trending <span className="badge">Array O(1)</span></h3>
          <ul style={{ paddingLeft: '20px', margin: 0 }}>
            {trends.filter(Boolean).length === 0 ? <li style={{ color: 'var(--text-muted)' }}>No trends generated yet</li> : null}
            {trends.filter(Boolean).map((t, i) => <li key={i} style={{ padding: '4px 0', color: 'var(--text-primary)' }}>{t}</li>)}
          </ul>
        </div>
      </div>

      {/* CENTER: Prefix Trie + DLL */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>

        {/* Navbar / Search */}
        <div className="panel animate-enter" style={{ display: 'flex', gap: '15px', alignItems: 'center', position: 'relative' }}>
          <b style={{ fontSize: '1.2rem', color: 'var(--primary)' }}>ConnectHub</b>
          <div style={{ flex: 1, position: 'relative', margin: '0 10px' }}>
            <input
              placeholder="Search users... (Prefix Trie O(m))"
              value={searchQ}
              onChange={e => handleSearch(e.target.value)}
            />
            {searchResults.length > 0 && (
              <div className="panel" style={{ position: 'absolute', top: '100%', left: 0, width: '100%', zIndex: 10, marginTop: '5px', padding: '10px', display: 'flex', flexDirection: 'column', gap: '10px' }}>
                {searchResults.map(s => (
                  <div key={s} style={{ cursor: 'pointer', padding: '8px', background: 'var(--bg)', borderRadius: '6px' }}>🔍 {s}</div>
                ))}
              </div>
            )}
          </div>
          <button style={{ background: 'var(--danger)' }} onClick={onLogout}>Exit</button>
        </div>

        {/* Create Post */}
        <CreatePost fetchFeed={fetchFeed} />

        {/* Feed List */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
          <h2 style={{ margin: 0, display: 'flex', alignItems: 'center', gap: '10px' }}>
            {currentProfile ? `${currentProfile}'s Posts` : 'News Feed'} <span className="badge">DLL O(1) Prepend</span>
          </h2>
          {feed.length === 0 && <p style={{ color: 'var(--text-muted)' }}>No posts to display. Be the first!</p>}
          {feed.map(p => (
            <PostCard key={p.id} post={p} user={user} />
          ))}
        </div>

      </div>

      {/* RIGHT SIDEBAR: BFS Graph + FIFO Queue */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>

        <div className="panel animate-enter" style={{ animationDelay: '0.2s' }}>
          <h3 style={{ margin: '0 0 15px 0' }}>Network <span className="badge">Graph Edges</span></h3>
          <div style={{ marginBottom: '20px' }}>
            <h4 style={{ margin: '0 0 10px 0', fontSize: '0.9rem', color: 'var(--text-muted)' }}>Suggested (BFS - Depth 2)</h4>
            {suggestions.length === 0 ? <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>No suggestions</p> : null}
            {suggestions.map(s => (
              <div key={s.id} style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px', alignItems: 'center' }}>
                <span style={{ fontSize: '0.95rem' }}>{s.username}</span>
                <button style={{ padding: '6px 12px', fontSize: '0.8rem' }} onClick={() => addFriend(s.id)}>Add Edge</button>
              </div>
            ))}
          </div>
          <div>
            <h4 style={{ margin: '0 0 10px 0', fontSize: '0.9rem', color: 'var(--text-muted)' }}>My Friends</h4>
            {friends.length === 0 ? <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>No edges exist</p> : null}
            {friends.map(f => (
              <div key={f.id} style={{ padding: '5px 0', fontSize: '0.95rem', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <div style={{ width: '8px', height: '8px', borderRadius: '50%', background: 'var(--success)' }} />
                {f.username}
              </div>
            ))}
          </div>
        </div>

        <div className="panel animate-enter" style={{ animationDelay: '0.3s' }}>
          <h3 style={{ margin: '0 0 15px 0' }}>Alerts <span className="badge">FIFO Queue</span></h3>
          {notifications.length === 0 && <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>Queue is empty...</p>}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
            {notifications.map((n, i) => (
              <div key={i} className="animate-enter" style={{ padding: '12px', background: 'rgba(99,102,241,0.1)', borderLeft: '4px solid var(--primary)', borderRadius: '6px', fontSize: '0.9rem', lineHeight: 1.4 }}>
                {n}
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
}

function CreatePost({ fetchFeed }) {
  const [content, setContent] = useState('');

  const submit = async (e) => {
    e.preventDefault();
    if (!content.trim()) return;
    await api.post('/posts', { content });
    setContent('');
    fetchFeed();
  };

  return (
    <form className="panel animate-enter" onSubmit={submit} style={{ display: 'flex', gap: '15px' }}>
      <input
        placeholder="What's happening? (Prepends to DLL head)"
        value={content}
        onChange={e => setContent(e.target.value)}
        style={{ fontSize: '1.05rem' }}
      />
      <button type="submit" style={{ padding: '0 25px' }}>Post</button>
    </form>
  );
}

function PostCard({ post, user }) {
  const [liked, setLiked] = useState(false);

  const toggleLike = async () => {
    if (liked) {
      await api.delete(`/posts/${post.id}/like`);
      setLiked(false);
    } else {
      await api.post(`/posts/${post.id}/like`);
      setLiked(true);
    }
  };

  return (
    <div className="panel animate-enter" style={{ position: 'relative' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '15px' }}>
        <div style={{ width: '38px', height: '38px', borderRadius: '50%', background: 'linear-gradient(135deg, var(--primary), var(--primary-hover))' }} />
        <div style={{ display: 'flex', flexDirection: 'column' }}>
          <span style={{ fontWeight: 600, fontSize: '1.05rem' }}>User ID {post.authorId}</span>
          <span style={{ color: 'var(--text-muted)', fontSize: '0.8rem' }}>{new Date(post.createdAt).toLocaleString()}</span>
        </div>
      </div>
      <p style={{ margin: '0 0 20px 0', lineHeight: 1.6, fontSize: '1.05rem', color: '#e2e8f0' }}>{post.content}</p>
      <div style={{ borderTop: '1px solid var(--border)', paddingTop: '15px', display: 'flex', gap: '15px' }}>
        <button onClick={toggleLike} style={{ background: liked ? 'rgba(239, 68, 68, 0.1)' : 'transparent', color: liked ? 'var(--danger)' : 'var(--text-muted)', border: `1px solid ${liked ? 'var(--danger)' : 'var(--border)'}`, padding: '6px 16px' }}>
          {liked ? '❤ Liked (Undo via Stack)' : '♡ Like (Emit to Queue)'}
        </button>
      </div>
    </div>
  );
}
