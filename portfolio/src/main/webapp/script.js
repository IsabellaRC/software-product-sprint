// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['I am almost a US citizen!', 'I have been chased by cows haha!', 'I have never broken any bones!', 'My favorite color is green!', 'I love to dance!', 'I have never had a pet!', ' I love corgis!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function getMessage() {
    const fetchPromise = fetch('/data');
    fetchPromise.then(handleResponse);
}

function handleResponse(response) {
    const responsePromise = response.text();
    responsePromise.then(createMessage);
}

function createMessage(messageText) {
    const message = document.getElementById('greeting-container');
    message.innerHTML = messageText;
}

function fetchFromServer() {
    fetch('/data').then(response => response.json()).then((jsonMessage) => {

    const dogListElement = document.getElementById('greeting-container');
    dogListElement.innerHTML = '';
    dogListElement.appendChild(
        createListElement('Dog 1: ' + jsonMessage[0]));
    dogListElement.appendChild(
        createListElement('Dog 2: ' + jsonMessage[1]));
    dogListElement.appendChild(
        createListElement('Dog 3: ' + jsonMessage[2]));
    });
}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}