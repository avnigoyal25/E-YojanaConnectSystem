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
  let botResponse = "Try Again!!!!!";

  if (lowercaseMessage.includes("website")) {
    botResponse = "This website helps you to find the best yojana for yourself";
  } else if (lowercaseMessage.includes("yojana", "schemes")) {
    botResponse = "There are yojanas for education, healthcare, agriculture, for girl child, minority group, BPL group & for physically challenged people";
  } else if (lowercaseMessage.includes("scheme", "yojanas")) {
    botResponse = "To know schemes fill up the form and submit it";
  }  else if (lowercaseMessage.includes("who made this site")) {
    botResponse = "This is designed by Manas, Avni & Mahak";
  } else if (lowercaseMessage.includes("thank you")) {
    botResponse = "welcome";
  } else if (lowercaseMessage.includes("ok")) {
    botResponse = "Ok";
  } else if (lowercaseMessage.includes("hi", "hello", "Hi", "Hey", "Hello")) {
    botResponse = "Hey, I'm your Sahayak";
  } else if (lowercaseMessage.includes("help")) {
    botResponse = "Tell me how can I help";
  } else if (lowercaseMessage.includes("name", "site")) {
    botResponse = "This is yojanaKHOJ, designed and developed for the betterment of the people who want to know about the schemes";
  } else if (lowercaseMessage.includes("govt.", "government", "legal", "safe", "illegal", "private")) {
    botResponse = "This is a minor project by the students of UPES";
  } 

  return botResponse;
}

function submitForm(event) {
    event.preventDefault(); // Prevent the default form submission

    // Get user input
    var age = document.querySelector('input[name="age"]').value;
    var income = parseFloat(document.querySelector('input[name="income"]').value);
    var gender = document.querySelector('input[name="gender"]:checked').value;
    var caste = document.querySelector('input[name="caste"]').value;
    var minority = document.querySelector('input[name="minority"]:checked').value;
    var physicalChallenge = document.querySelector('input[name="physical_challenge"]:checked').value;

    // Store user input in local storage
    localStorage.setItem('userInput', JSON.stringify({
        age: age,
        income: income,
        gender: gender,
        caste: caste,
        minority: minority,
        physicalChallenge: physicalChallenge
    }));
}

// JavaScript code for handling the "Find the best yojana" button click
document.querySelector('form').addEventListener('submit', function (e) {
    e.preventDefault(); // Prevent the form from submitting

    // Simulate an API call to retrieve yojanas (replace with actual data retrieval)
    setTimeout(function () {
        const yojanas = [
            { name: 'Yojana 1', description: 'Description of Yojana 1' },
            { name: 'Yojana 2', description: 'Description of Yojana 2' },
            { name: 'Yojana 3', description: 'Description of Yojana 3' }
            // Add more yojanas as needed
        ];

        displayYojanas(yojanas);
    }, 2000); // Simulating a 2-second delay (you can adjust this)

    // You can also make an actual AJAX request to your server to fetch yojana data.
});

// Function to display yojanas in different containers
function displayYojanas(yojanas) {
    const yojanasContainer = document.createElement('div');
    yojanasContainer.classList.add('yojanas-container');

    yojanas.forEach((yojana, index) => {
        const yojanaCard = document.createElement('div');
        yojanaCard.classList.add('yojana-card');

        const yojanaName = document.createElement('h2');
        yojanaName.textContent = yojana.name;

        const yojanaDescription = document.createElement('p');
        yojanaDescription.textContent = yojana.description;

        const applyButton = document.createElement('button');
        applyButton.textContent = 'Apply Now';

        yojanaCard.appendChild(yojanaName);
        yojanaCard.appendChild(yojanaDescription);
        yojanaCard.appendChild(applyButton);

        yojanasContainer.appendChild(yojanaCard);
    });

    document.body.appendChild(yojanasContainer);
}

// Close the chat popup
document.getElementById('closeBtn').addEventListener('click', function () {
    document.getElementById('chatPopup').style.display = 'none';
});

