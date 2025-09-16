document.addEventListener("DOMContentLoaded", () => {
  // Default tab
  openTab("users");

  // Load leaderboard dropdown
  loadLeaderboardQuizzes();

  // Attach form event listeners
  document.getElementById("addUserForm").addEventListener("submit", addUser);
  document.getElementById("addQuizForm").addEventListener("submit", addQuiz);
  document
    .getElementById("addQuestionForm")
    .addEventListener("submit", addQuestion);

  // Tab switching
  document.querySelectorAll(".tab-link").forEach((link) => {
    link.addEventListener("click", () => openTab(link.dataset.tab));
  });
});

/* ===================== TAB HANDLING ===================== */
function openTab(tabName) {
  document
    .querySelectorAll(".tab-content")
    .forEach((tc) => tc.classList.remove("active"));
  document
    .querySelectorAll(".tab-link")
    .forEach((tl) => tl.classList.remove("active"));

  document.getElementById(tabName).classList.add("active");
  document
    .querySelector(`.tab-link[data-tab="${tabName}"]`)
    .classList.add("active");

  if (tabName === "users") loadUsers();
  if (tabName === "quizzes") loadQuizzes();
  if (tabName === "questions") {
    // Load quizzes first (for dropdown)
    loadQuizOptions().then(() => {
      // Then load questions
      loadQuestionsForAdmin();
    });
  }
}

/* ===================== USERS ===================== */

function loadUsers() {
  fetch("/api/admin/users")
    .then((res) => res.json())
    .then((users) => {
      const list = document.getElementById("users-list");
      if (!users || users.length === 0)
        return (list.innerHTML = "<p>No users found.</p>");
      list.innerHTML = users
        .map(
          (u) => `
        <div class="list-item">
          <div>
            <h4>${u.username}</h4>
            <p>${u.email} — ${u.role}</p>
          </div>
          <div class="card-actions">
            <button class="edit" onclick="editUser(${u.id}, '${u.username}', '${u.email}', '${u.role}')">Edit</button>
            <button class="delete" onclick="deleteUser(${u.id})">Delete</button>
          </div>
        </div>
      `
        )
        .join("");
    });
}

function addUser(e) {
  e.preventDefault();
  const user = {
    username: document.getElementById("username").value,
    email: document.getElementById("email").value,
    password: document.getElementById("password").value,
    role: document.getElementById("role").value,
  };

  fetch("/api/admin/users", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(user),
  }).then(() => {
    e.target.reset();
    loadUsers();
    alert("User added successfully!");
  });
}

function editUser(id, username, email, role) {
  const newUsername = prompt("Update username:", username);
  const newEmail = prompt("Update email:", email);
  const newRole = prompt("Update role (USER/ADMIN):", role);

  if (newUsername && newEmail && newRole) {
    fetch(`/api/admin/users/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: newUsername,
        email: newEmail,
        role: newRole,
      }),
    }).then(() => loadUsers());
  }
}

function deleteUser(id) {
  if (confirm("Are you sure you want to delete this user?")) {
    fetch(`/api/admin/users/${id}`, { method: "DELETE" }).then(() =>
      loadUsers()
    );
  }
}

/* ===================== QUIZZES ===================== */

function loadQuizzes() {
  fetch("/api/admin/quizzes")
    .then((res) => res.json())
    .then((quizzes) => {
      const container = document.getElementById("quizzes-list");
      if (!quizzes || quizzes.length === 0)
        return (container.innerHTML = "<p>No quizzes found.</p>");
      container.innerHTML = quizzes
        .map(
          (q) => `
        <div class="list-item">
          <div>
            <h4>${q.title}</h4>
            <p>Category: ${q.category || "General"}</p>
          </div>
          <div class="card-actions">
            <button class="edit" onclick="editQuiz(${q.id}, '${q.title}', '${
            q.category || ""
          }')">Edit</button>
            <button class="delete" onclick="deleteQuiz(${q.id})">Delete</button>
          </div>
        </div>
      `
        )
        .join("");
    });
}

function addQuiz(e) {
  e.preventDefault();
  const quiz = {
    title: document.getElementById("quizTitle").value,
    category: document.getElementById("quizCategory").value || "General",
  };

  fetch("/api/admin/quizzes", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(quiz),
  }).then(() => {
    e.target.reset();
    loadQuizzes();
    loadQuizOptions();
  });
}

function loadQuizOptions() {
  return fetch("/api/admin/quizzes")
    .then((res) => res.json())
    .then((quizzes) => {
      const select = document.getElementById("quizSelect");
      select.innerHTML = quizzes
        .map((q) => `<option value="${q.id}">${q.title}</option>`)
        .join("");
    });
}

function editQuiz(id, title, category, active) {
  const newTitle = prompt("Update quiz title:", title);
  const newCategory = prompt("Update quiz category:", category);
  const isActive = confirm("Should this quiz be active?");

  if (newTitle) {
    fetch(`/api/admin/quizzes/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        title: newTitle,
        category: newCategory,
        active: isActive,
      }),
    }).then(() => loadQuizzes());
  }
}

function deleteQuiz(id) {
  if (confirm("Are you sure you want to delete this quiz?")) {
    fetch(`/api/admin/quizzes/${id}`, { method: "DELETE" }).then(() =>
      loadQuizzes()
    );
  }
}

/* ===================== QUESTIONS ===================== */

function loadQuestionsForAdmin() {
  fetch("/api/admin/questions")
    .then((res) => res.json())
    .then((questions) => {
      const container = document.getElementById("questions-list");
      if (!questions || questions.length === 0) {
        container.innerHTML = "<p>No questions found.</p>";
        return;
      }

      container.innerHTML = questions
        .map(
          (q) => `
        <div class="list-item">
          <div>
            <h4>${q.questionText}</h4>
            <p><strong>Quiz:</strong> ${q.quizTitle || "Unknown"}</p>
            <p>A: ${q.optionA}, B: ${q.optionB}, C: ${q.optionC}, D: ${
            q.optionD
          }</p>
            <p><strong>Answer:</strong> ${q.correctAnswer}</p>
          </div>
          <div class="card-actions">
            <button class="edit" onclick="editQuestion(
                  ${q.id},
                  '${q.questionText.replace(/'/g, "\\'")}',
                  '${q.optionA.replace(/'/g, "\\'")}',
                  '${q.optionB.replace(/'/g, "\\'")}',
                  '${q.optionC.replace(/'/g, "\\'")}',
                  '${q.optionD.replace(/'/g, "\\'")}',
                  '${q.correctAnswer.replace(/'/g, "\\'")}',
                  ${q.quizId || 0}
                )">Edit</button>
            <button class="delete" onclick="deleteQuestion(${
              q.id
            })">Delete</button>
          </div>
        </div>
      `
        )
        .join("");
    })
    .catch(() => {
      document.getElementById("questions-list").innerHTML =
        "<p>Error loading questions.</p>";
    });
}

function addQuestion(e) {
  e.preventDefault();

  const quizId = parseInt(document.getElementById("quizSelect").value, 10);

  if (!quizId) {
    alert("Please select a quiz first!");
    return;
  }

  const question = {
    questionText: document.getElementById("questionText").value,
    optionA: document.getElementById("optionA").value,
    optionB: document.getElementById("optionB").value,
    optionC: document.getElementById("optionC").value,
    optionD: document.getElementById("optionD").value,
    correctAnswer: document.getElementById("answer").value,
    quiz: { id: quizId }, // number, not string/null
  };

  console.log("Sending question payload:", question);

  fetch("/api/admin/questions", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(question),
  })
    .then((res) => {
      if (!res.ok) throw new Error("Failed to save question");
      return res.json();
    })
    .then((data) => {
      console.log("Question saved:", data);
      document.getElementById("addQuestionForm").reset();
      loadQuestionsForAdmin();
    })
    .catch((err) => {
      console.error(err);
      alert("Error adding question. Make sure a quiz exists and is selected.");
    });
}

function editQuestion(id, text, a, b, c, d, answer, quizId) {
  const newText = prompt("Update question:", text);
  const newA = prompt("Update option A:", a);
  const newB = prompt("Update option B:", b);
  const newC = prompt("Update option C:", c);
  const newD = prompt("Update option D:", d);
  const newAnswer = prompt("Update answer:", answer);
  const newQuizId = Number(prompt("Quiz ID for this question:", quizId || 0));

  if (newText && newA && newB && newC && newD && newAnswer && newQuizId) {
    fetch(`/api/admin/questions/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        questionText: newText,
        optionA: newA,
        optionB: newB,
        optionC: newC,
        optionD: newD,
        correctAnswer: newAnswer,
        quiz: { id: newQuizId },
      }),
    }).then(() => loadQuestionsForAdmin());
  }
}

function deleteQuestion(id) {
  if (confirm("Are you sure you want to delete this question?")) {
    fetch(`/api/admin/questions/${id}`, { method: "DELETE" }).then(() =>
      loadQuestionsForAdmin()
    );
  }
}

/* ===================== LEADERBOARD ===================== */
// Load quiz options
function loadLeaderboardQuizzes() {
  fetch("/api/quizzes")
    .then((res) => res.json())
    .then((data) => {
      console.log("Quizzes fetched:", data); //  Debug
      const select = document.getElementById("leaderboardQuizSelect");
      select.innerHTML = "";
      data.forEach((quiz) => {
        const option = document.createElement("option");
        option.value = quiz.id;
        option.textContent = quiz.title;
        select.appendChild(option);
      });
      if (data.length > 0) {
        loadLeaderboard(data[0].id);
      }
    });
}

function loadLeaderboard() {
  let quizId = document.getElementById("leaderboardQuizSelect").value;
  fetch(`/api/leaderboard/quiz/${quizId}`)
    .then((res) => res.json())
    .then((data) => {
      let table = document.getElementById("leaderboard-table");
      table.innerHTML = data
        .map(
          (entry) => `
                        <tr>
                            <td>${entry.username}</td>
                            <td>${entry.score}</td>
                            <td>${entry.totalMarks}</td>
                        </tr>
                    `
        )
        .join("");
    });
}

document.addEventListener("DOMContentLoaded", () => {
  loadLeaderboardQuizzes();

  document
    .getElementById("leaderboardQuizSelect")
    .addEventListener("change", (e) => {
      loadLeaderboard(e.target.value);
    });
});
