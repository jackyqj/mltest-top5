import React, {Component} from 'react';
import axios from 'axios';
import DatePicker from 'react-datepicker';
import dateFormat from 'dateformat';
import moment from 'moment';
import 'react-datepicker/dist/react-datepicker.css';
import './mltest.css'

import {Table, Grid, Row, Col} from 'react-bootstrap';


class App extends Component {

    constructor(props) {
        super(props);
        var todayStr = dateFormat(new Date(), 'yyyy-mm-dd');
        this.state = {
            visitHistories: [{
                "id": 341,
                "recordDate": 1452009600000,
                "website": "www.google.com.au",
                "visitCount": 151749278,
                "processDate": 1517041922000
            }, {
                "id": 307,
                "recordDate": 1452009600000,
                "website": "www.facebook.com",
                "visitCount": 104346720,
                "processDate": 1517041922000
            }, {
                "id": 333,
                "recordDate": 1452009600000,
                "website": "www.youtube.com",
                "visitCount": 59811438,
                "processDate": 1517041922000
            }, {
                "id": 312,
                "recordDate": 1452009600000,
                "website": "www.google.com",
                "visitCount": 26165099,
                "processDate": 1517041922000
            }, {
                "id": 315,
                "recordDate": 1452009600000,
                "website": "ninemsn.com.au",
                "visitCount": 21734381,
                "processDate": 1517041922000
            }],
            checkedDate: todayStr,
            startDate: moment()
        };

        this.onCheckedDateChange = this.onCheckedDateChange.bind(this);
    }


    //Handle the date change event, whenever user changed the date in the calendar, we call the backend service to get the new data
    onCheckedDateChange = (date) => {
        console.log(date);
        if (!date) {
            return;
        }
        var dateStr = dateFormat(date, 'yyyy-mm-dd');
        console.log(dateStr);
        this.setState({
            checkedDate: dateStr,
            startDate: date
        })

        axios.get('./histories/' + dateStr)
            .then(res => {
                console.debug(res.data);
                const data = res.data;
                this.setState({
                    visitHistories: data
                });
            })
            .catch(err => {
                console.log(err.stack);
                this.setState({
                    visitHistories: []
                });
            });
    }

    render() {
        return (
            <div class="container-fluid">
                <Grid>
                    <Row className="show-grid">
                        <Col xs={12} md={8}>
                            <div class="jumbotron">
                                <h2 class="display-4">Top 5 Website</h2>
                                <p class="lead">This is to show the top 5 visit websites based on the date you selected.
                                    You can change a different date to refresh the report by clicking the date
                                    button.</p>
                                <DatePicker
                                    customInput={<DateButton />}
                                    selected={this.state.startDate}
                                    value={this.state.checkedDate}
                                    peekNextMonth
                                    showMonthDropdown
                                    showYearDropdown
                                    maxDate={moment()}
                                    onChange={this.onCheckedDateChange} />
                                <hr class="my-4"/>
                                <VisitHistoryList vhList={this.state.visitHistories}></VisitHistoryList>
                            </div>
                        </Col>
                    </Row>
                </Grid>
            </div>
        )
    }
}

class DateButton extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <p class="lead">
                <a class="btn btn-primary btn-md" href="#"
                   onClick={this.props.onClick}
                   role="button">{this.props.value}</a>
            </p>
        )
    }
}
class VisitHistoryList extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {

        let idx = 0;
        var vHistoryList = this.props.vhList.map(vHis =>
            <VisitHistory history={vHis} idx={++idx}/>
        );
        console.log(idx, vHistoryList.length);

        if (vHistoryList && vHistoryList.length > 0) {
            return (
                <div>
                    <Table striped bordered condensed hover>
                        <tbody>
                        <tr>
                            <th>Sequence</th>
                            <th>Website</th>
                            <th>Visit Count</th>
                        </tr>
                        {vHistoryList}
                        </tbody>
                    </Table>
                    <div>
                    </div>
                </div>
            )
        } else {
            return (
                <div class="alert alert-info" role="alert">
                    Oops! There is no report data for the selected date!<br/>
                    You may try (2016-01-06), (2016-03-13), (2016-12-06)
                </div>
            )
        }
    }
}


class VisitHistory extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        console.log(this.props.history);
        return (
            <tr>
                <td>{this.props.idx}</td>
                <td>{this.props.history.website}</td>
                <td>{this.props.history.visitCount}</td>
            </tr>
        )
    }
}

export default App;
