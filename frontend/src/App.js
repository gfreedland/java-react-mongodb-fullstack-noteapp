import React from 'react';
import './App.css';
import axios from 'axios';

class App extends React.Component{
  //Not necesrrary because using Stateful component
  //const [id, setid] = React.useState('');
  //const [note, setNote] = React.useState('');
  //const [data, setData] = React.useState('');

  state = {
    id: '',
    note: '',
    data: ''
  };

  componentDidMount() {
    axios.get(`http://localhost:1234/list`)  //promise
      .then((res) => {
        // .log(res.data.response);
        const newData = res.data.response.map((obj, index) => {
          return (
            <div key={index} style={{border: '1px solid', padding: '10px', boxshadow: '5px 10px', margin: '10px'}}>
              <p>_id = {obj._id}</p>
              <p>note = {obj.data}</p>
            </div>
          );
        })
        this.setState({ ...this.state, data: newData })
      })
      .catch((error) => {
        console.log(error);
      });
  }

  render(){

  const save = (note, id) => {
    if(id === '') {
      axios.post('http://localhost:1234/store', note)
        .then(console.log)
        .catch(console.log);
    } else {
      axios.post(`http://localhost:1234/update?_id=${id}`, note)
        .then(console.log)
        .catch(console.log);
    }
    refresh();
    refresh();
  };

  const deleteNote = (id) => {
    axios.post(`http://localhost:1234/delete?_id=${id}`)  //promise
      .then((res) => {
        console.log(res);
        console.log(res.data);
      })
      .catch(console.log);
    refresh();
    refresh();
  };

  const refresh = () => {
    console.log("fetching data");
    axios.get(`http://localhost:1234/list`)  //promise
      .then((res) => {
        console.log(res.data.response);
        const newData = res.data.response.map((obj, index) => {
          return (
            <div key={index} style={{border: '1px solid', padding: '10px', boxshadow: '5px 10px', margin: '10px'}}>
              <p>_id = {obj._id}</p>
              <p>note = {obj.data}</p>
            </div>
          );
        })
        this.setState({...this.state, data: newData });
        //setData(newData);
        //return newData;
      })
      .catch((error) => {
        console.log(error);
      });
      // console.log(newData);
  }

  return (
    <div className="App">
      {/*}<p>{id}</p>
      <p>{note}</p>*/}

      <div style={{ display : 'inline-block', width: '20%', height: '50%' }}>
        <input type="text" placeholder="_id box" value={this.state.id} onChange={e => this.setState({...this.state, id: e.target.value})}/>
        <textarea placeholder="Text Box" value={this.state.note} onChange={e => this.setState({...this.state, note: e.target.value})}></textarea>
      </div>
        <div>
          <button onClick={() => save(this.state.note, this.state.id)}>Save</button>
          <button onClick={() => deleteNote(this.state.id)}>Delete</button>
          {/*  NOT NECESSARY BUTTON BECAUSE APP FETCHES ON MOUNT
            <button onClick={() => refresh()}>Refresh</button>
          */}
        </div>
        <br/>
      <div style={{border: '1px solid', padding: '10px', boxshadow: '5px 10px'}}>
        <h1>Fetched notes from db</h1>
        <div>
          <div>{this.state.data}</div>
        </div>

      </div>
    </div>
  );
}
}

export default App;
