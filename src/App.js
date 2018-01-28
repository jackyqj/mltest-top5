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
            visitHistories: [],
            visitFilters: [],
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
            .then(response => {
                var respData = response.data;
                this.setState({
                    visitHistories: respData.histories,
                    visitFilters: respData.filters
                });
            })
            .catch(err => {
                console.log(err.stack);
                this.setState({
                    visitHistories: [],
                    visitFilters: []
                });
            });
    }

    render() {
        return (
            <div className="container-fluid">
                <Grid>
                    <Row className="show-grid">
                        <Col xs={16} md={8}>
                            <div className="jumbotron">
                                <table>
                                    <tbody>
                                    <tr>
                                        <td valign="top">
                                            <h2 className="display-4">Top 5 Website</h2>
                                            <p className="lead">This is to show the top 5 visit websites based on the date you selected.
                                                You can change a different date to refresh the report by changing the date
                                                in the calendar on the right...</p>
                                            <br/>
                                            <h5>Below shows the top 5 visit report on <u><i><font color="blue">{this.state.checkedDate}</font></i></u></h5>
                                        </td>
                                        <td valign="top">
                                            <DatePicker
                                                inline
                                                selected={this.state.startDate}
                                                showMonthDropdown
                                                showYearDropdown
                                                maxDate={moment()}
                                                onChange={this.onCheckedDateChange}
                                            />
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <VisitHistoryList vhList={this.state.visitHistories} checkedDate={this.state.checkedDate}></VisitHistoryList>
                                <FilterInfoBox value={this.state.visitFilters}/>
                            </div>
                        </Col>
                    </Row>
                </Grid>
            </div>
        )
    }
}

class FilterInfoBox extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {

        var vFilterList = this.props.value.map(vFil =>
            <VisitFilter vFilter={vFil}/>
        );
        if (vFilterList && vFilterList.length > 0) {
            return (
                <div className="alert alert-info text-left" role="alert">
                    <span>Filter was applied in this report:</span>
                    {vFilterList}
                </div>
            )
        } else {
            return (<span></span>);
        }
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
                <div className="alert alert-warning text-left" role="alert">
                    Oops! There is no report data for the selected date <u>{this.props.checkedDate}</u>!<br/>
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

class VisitFilter extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        var filterStr = ' [Website: ' + this.props.vFilter.website;
        filterStr += this.props.vFilter.dateFrom ? ', From: ' + dateFormat(this.props.vFilter.dateFrom, "yyyy-mm-dd") : '';
        filterStr += this.props.vFilter.dateTo ? ', To: ' + dateFormat(this.props.vFilter.dateTo, "yyyy-mm-dd") : '';
        filterStr += '.] ';
        return (
            <span>{filterStr}</span>
        )
    }
}

export default App;
