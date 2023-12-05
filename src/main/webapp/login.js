const loginToggle = document.getElementById("loginToggle");
const registerToggle = document.getElementById("registerToggle");
const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const otpContainer = document.getElementById("otpContainer");
const verifyOTPButton = document.getElementById("verifyOTPButton");

loginToggle.addEventListener("click", () => {
  loginForm.style.display = "block";
  registerForm.style.display = "none";
  otpContainer.style.display = "none";
});

registerToggle.addEventListener("click", () => {
  loginForm.style.display = "none";
  registerForm.style.display = "block";
  otpContainer.style.display = "none";
});

// Simulated OTP generation and validation
const generateOTP = () => {
  return Math.floor(1000 + Math.random() * 9000);
};

let currentOTP;

const sendOTP = () => {
  currentOTP = generateOTP();
  alert(`Simulated OTP: ${currentOTP}`);
  otpContainer.style.display = "block";
};

verifyOTPButton.addEventListener("click", () => {
  const otpInput = document.getElementById("otpInput").value;
  if (otpInput === currentOTP.toString()) {
    alert("OTP verification successful. Registration complete.");
    loginForm.style.display = "block";
    registerForm.style.display = "none";
    otpContainer.style.display = "none";
  } else {
    alert("Incorrect OTP. Please try again.");
  }
});

document.getElementById("registerButton").addEventListener("click", () => {
  sendOTP();
});













const chatIcon = document.getElementById("chatIcon");
const chatPopup = document.getElementById("chatPopup");
const closeBtn = document.getElementById("closeBtn");
const sendBtn = document.getElementById("sendBtn");
const chatContent = document.getElementById("chatContent");
const userInput = document.getElementById("userInput");

chatIcon.addEventListener("click", () => {
  chatPopup.style.display = "block";
});

closeBtn.addEventListener("click", () => {
  chatPopup.style.display = "none";
});

sendBtn.addEventListener("click", () => {
  const userMessage = userInput.value;
  if (userMessage.trim() !== "") {
    appendMessage(userMessage, "user");
    const botResponse = generateBotResponse(userMessage);
    appendMessage(botResponse, "bot");
    userInput.value = "";
  }
});

function appendMessage(message, sender) {
  const messageDiv = document.createElement("div");
  messageDiv.className = sender + "-message";
  messageDiv.textContent = message;
  chatContent.appendChild(messageDiv);
  chatContent.scrollTop = chatContent.scrollHeight;
}

function generateBotResponse(userMessage) {
  const lowercaseMessage = userMessage.toLowerCase();
  let botResponse = "Hello........";

  if (lowercaseMessage.includes("website")) {
    botResponse = "This website helps you to find the best yojana for yourself";
  } else if (lowercaseMessage.includes("yojana", "schemes")) {
    botResponse = "There are yojanas for education, healthcare, agriculture, for girl child, minority group, BPL group & for physically challenged people";
  } else if (lowercaseMessage.includes("scheme")) {
    botResponse = "To know schemes fill up the form and submit it";
  }  else if (lowercaseMessage.includes("who made this site")) {
    botResponse = "This is designed by Avni, Mahak, Manas";
  } else if (lowercaseMessage.includes("thank you")) {
    botResponse = "welcome";
  } else if (lowercaseMessage.includes("ok")) {
    botResponse = "Ok";
  } else if (lowercaseMessage.includes("help")) {
    botResponse = "Tell me how can I help";
  } else if (lowercaseMessage.includes("name", "site")) {
    botResponse = "This is yojanaKHOJ, designed and developed for the betterment of the people who want to know about the schemes";
  } 

  return botResponse;
}
