import { useEffect, useState } from "react";
import axios from "axios";
import CopyToClipboard from "react-copy-to-clipboard";

const LinkResult = ({inputValue}) => {
  console.log("test");
  const [shortenLink, setShortenLink] = useState("");
  const [copied, setCopied] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(false);
  const fetchData = async()=>{
    try{
      setLoading(true);
      const res = await axios.post(`http://localhost:8080/api/shorten`,{
        inputValue
      });
      setShortenLink(`http://localhost:8080/api/${res.data}`);
    }catch(err){
      setError(err);
    }finally{
      setLoading(false);
    }
}

  useEffect(()=>{
    if(inputValue.length){
      fetchData();
    }
  },[inputValue]);



  useEffect(() => {
    const timer = setTimeout(() => {
      setCopied(false);
    }, 1000);
    return () => clearTimeout(timer);
  }, [copied]);

  if(loading){
    return <p className="noData">Loading....</p>
  }

  if(error){
    return <p className="noData">Something went wrong!!.</p>
  }

  
  return (
    <>
    {shortenLink && (
      <div className="result">
      <p>{shortenLink}</p>
      <CopyToClipboard text={shortenLink} onCopy={() => setCopied(true)}>
        <button className={copied ? "copied" : ""}>Copy to clipboard</button>
      </CopyToClipboard>
    </div>
    )}
    </>
    
  );
};

export default LinkResult;
