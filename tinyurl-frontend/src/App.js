import './App.css';
import UrlShortner from './components/UrlShortner';
import BackgroundAnimate from './components/BackgroundAnimate';
import LinkResult from './components/LinkResult';
import { useState } from 'react';

function App() {
  const [inputValue, setInputValue] = useState("");
  return (
    <div className="container">
      <UrlShortner setInputValue={setInputValue}/>
      <BackgroundAnimate/>
      <LinkResult inputValue={inputValue}/>
    </div>
  );
}

export default App;
