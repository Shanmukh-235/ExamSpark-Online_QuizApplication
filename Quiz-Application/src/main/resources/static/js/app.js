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
                    <input type="radio" name="q${q.id}" value="${q.option4}"> ${q.option4}
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
    inputs.forEach(input => {
        const questionId = input.name.replace("q", "");
        answers[questionId] = input.value;
    });

    fetch(`/api/quizzes/${quizId}/questions`)
        .then(res => res.json())
        .then(questions => {
            let score = 0;
            questions.forEach(q => {
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
                    user: { id: user.id }
                })
            })
            .then(res => res.json())
            .then(() => {
                // Redirect to leaderboard after submission
                window.location.href = `/leaderboard.html?quizId=${quizId}`;
            })
            .catch(err => console.error("Error saving result:", err));
        });
}

//Load leaderboard on leaderboard.html

function loadLeaderboard(quizId) {
  fetch(`/api/results/quiz/${quizId}/leaderboard`)
    .then(res => res.json())
    .then(results => {
      const container = document.getElementById("leaderboard-container");
      container.innerHTML = results
        .map((r, index) => `
          <p>${index + 1}. ${r.user.username} - ${r.score}</p>
        `)
        .join("");
    });
}

// Call this on leaderboard.html
document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("leaderboard-container")) {
    const urlParams = new URLSearchParams(window.location.search);
    const quizId = urlParams.get("quizId");
    loadLeaderboard(quizId);
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

// login ""

const email = document.getElementById("email").value;

fetch("/api/users/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
});
