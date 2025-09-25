// Detect which page we're on
document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("quiz-list")) {
    loadQuizzes();
  } else if (document.getElementById("questions-container")) {
    loadQuestions();
    document.getElementById("submit-btn").addEventListener("click", submitQuiz);
  }
});

// Load all quizzes on index.html
function loadQuizzes() {
  fetch("/api/quizzes")
    .then((res) => res.json())
    .then((quizzes) => {
      const container = document.getElementById("quiz-list");
      quizzes.forEach((quiz) => {
        const div = document.createElement("div");
        div.innerHTML = `
                    <h3>${quiz.title}</h3>
                    <p>Category: ${quiz.category || "General"}</p>
                    <button onclick="startQuiz(${quiz.id})">Start Quiz</button>
                `;
        container.appendChild(div);
      });
    });
}

function startQuiz(quizId) {
  window.location.href = `/quiz.html?quizId=${quizId}`;
}

// Load questions for a quiz on quiz.html
function loadQuestions() {
  const urlParams = new URLSearchParams(window.location.search);
  const quizId = urlParams.get("quizId");

  fetch(`/api/quizzes/${quizId}/questions`)
    .then((res) => res.json())
    .then((questions) => {
      const container = document.getElementById("questions-container");
      questions.forEach((q, idx) => {
        const div = document.createElement("div");
        div.innerHTML = `
                    <p>${idx + 1}. ${q.questionText}</p>
                    <input type="radio" name="q${q.id}" value="${q.option1}"> ${q.option1}
                    <br>
                    <input type="radio" name="q${q.id}" value="${q.option2}"> ${q.option2}
                    <br>
                    <input type="radio" name="q${q.id}" value="${q.option3}"> ${q.option3}
                    <br>
                    <input type="radio" name="q${q.id}" value="${q.option4}"> ${q.option4}s
                    <br>
                `;
        container.appendChild(div);
      });
    });
}

// Placeholder for submit functionality
function submitQuiz() {
  const urlParams = new URLSearchParams(window.location.search);
  const quizId = urlParams.get("quizId");

  const container = document.getElementById("questions-container");
  const inputs = container.querySelectorAll('input[type="radio"]:checked');

  if (inputs.length === 0) {
    alert("Please select at least one answer.");
    return;
  }

  const answers = {};
  inputs.forEach((input) => {
    const questionId = input.name.replace("q", "");
    answers[questionId] = input.value;
  });

  fetch(`/api/quizzes/${quizId}/questions`)
    .then((res) => res.json())
    .then((questions) => {
      let score = 0;
      questions.forEach((q) => {
        if (answers[q.id] === q.correctAnswer) score++;
      });

      alert(`You scored ${score} out of ${questions.length}`);

      const user = JSON.parse(localStorage.getItem("loggedInUser"));
      if (!user) return alert("Please log in first!");

      // Save result
      fetch("/api/results", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          score: score,
          quiz: { id: quizId },
          user: { id: user.id },
        }),
      })
        .then((res) => res.json())
        .then(() => {
          // Redirect to leaderboard after submission
          window.location.href = `/leaderboard.html?quizId=${quizId}`;
        })
        .catch((err) => console.error("Error saving result:", err));
    });
}

//Load leaderboard on leaderboard.html

// Load quiz options in dropdown for leaderboard.html
function loadLeaderboardQuizzes() {
  fetch("/api/admin/quizzes")
    .then((res) => res.json())
    .then((data) => {
      const select = document.getElementById("quizSelect");
      select.innerHTML = "";
      data.forEach((quiz) => {
        const option = document.createElement("option");
        option.value = quiz.id;
        option.textContent = quiz.title;
        select.appendChild(option);
      });
      if (data.length > 0) loadLeaderboard(data[0].id); // auto-load first quiz leaderboard
    });
}

// Load leaderboard data for a quiz
function loadLeaderboard(quizId) {
  fetch(`/api/leaderboard/quiz/${quizId}`)
    .then((res) => res.json())
    .then((data) => {
      const tbody = document.getElementById("leaderboard-table");
      tbody.innerHTML =
        data.length > 0
          ? data
              .map(
                (entry, index) => `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${entry.user.username}</td>
                        <td>${entry.score}</td>
                        <td>${entry.totalMarks}</td>
                    </tr>
                `
              )
              .join("")
          : `<tr><td colspan="4">No leaderboard data available yet.</td></tr>`;
    })
    .catch((err) => console.error("Error loading leaderboard:", err));
}

document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("quizSelect")) {
    loadLeaderboardQuizzes();
    document.getElementById("quizSelect").addEventListener("change", (e) => {
      loadLeaderboard(e.target.value);
    });
  }
});

// register

document
  .getElementById("register-form")
  ?.addEventListener("submit", function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    fetch("/api/users/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, email, password }),
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.id) {
          alert("Registration successful! Please log in.");
          window.location.href = "/login.html";
        } else {
          document.getElementById("register-message").innerText = data;
        }
      })
      .catch((err) => console.error(err));
  });

// login (safe)
document.getElementById("login-form")?.addEventListener("submit", function (e) {
  e.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  fetch("/api/users/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  })
    .then(res => res.json())
    .then(data => {
      if (data.id) {
        // Save logged in user in localStorage
        localStorage.setItem("loggedInUser", JSON.stringify(data));
        window.location.href = "/dashboard.html";
      } else {
        alert("Invalid credentials. Please try again.");
      }
    })
    .catch(err => console.error("Login error:", err));
});

// fetch("/api/users/login", {
//   method: "POST",
//   headers: { "Content-Type": "application/json" },
//   body: JSON.stringify({ email, password }),
// });

// Load user performance on performance.html
function loadPerformance() {
  const user = JSON.parse(localStorage.getItem("loggedInUser"));
if (!user) {
  alert("Please log in first!");
  window.location.href = "/login.html";
  return;
}

fetch(`/api/performance/${user.id}`)
  .then(res => res.json())
  .then(data => {
    document.getElementById("performance-title").textContent =
      `Performance of ${user.username}`;
    const tbody = document.getElementById("performance-table");

    if (!data || data.length === 0) {
      tbody.innerHTML = `<tr><td colspan="3">No performance data available yet.</td></tr>`;
      return;
    }

    tbody.innerHTML = data.map(p => `
      <tr>
        <td>${p.quizName}</td>
        <td>${p.score}</td>
        <td>${p.totalMarks}</td>
      </tr>
    `).join("");
  });
}

document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("performance-table")) {
    loadPerformance();
  }
});
