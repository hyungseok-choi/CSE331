/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';

interface BldSelectProps {
    onChange(edges: Edge[] | null): void;  // called when a new edge list is ready
}

interface BldSelectState {
    isBldLoaded: boolean;
    start: string;
    end: string;
    bldNames: string[];
}

type Edge = {
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    color: string;
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class BldSelect extends Component<BldSelectProps, BldSelectState> {

    constructor(props: BldSelectProps) {
        super(props);
        this.state = {
            isBldLoaded: false,
            start: "",
            end: "",
            bldNames: [],
        }
        this.getBldNames();
    }

    onClearClick(){
        this.setState({
            start: '',
            end: '',
        })
        this.props.onChange(null);
    }

    onDrawClick() : void{
        let boolean = this.validateEdge();
        if (!boolean){
            alert("Building not selected");
            return;
        }

        let url = new URL('http://localhost:4567/minpath');
        url.searchParams.append("s", this.state.start);
        url.searchParams.append("e", this.state.end);
        fetch(url.toString())
            .then(response => response.json()
                .then(data => {
                    let edge = this.parsePath(JSON.stringify(data));
                    this.props.onChange(edge);
                }))
    }

    parsePath(data: string) : Edge[] {
        let ret: Edge[] = [];
        let obj = JSON.parse(data);
        let color: string;
        let cost = parseInt(obj["cost"]);
        if (cost < 300) {
            color = "yellow";
        } else if (cost < 500){
            color = "orange";
        } else if (cost < 1500){
            color = "green";
        } else if (cost < 2500){
            color = "blue";
        } else if (cost < 3500){
            color = "black";
        } else {
            color = "red"
        }
        obj["path"].forEach((e: any) => {
            let edge: Edge = {
                x1: e['start']['x'],
                y1: e['start']['y'],
                x2: e['end']['x'],
                y2: e['end']['y'],
                color: color,
            }
            ret.push(edge);
        })
        return ret;
    }

    validateEdge(): boolean {
        return !(this.state.start === "" || this.state.end === "");
    }

    getBldNames(): void {
        if (!this.state.isBldLoaded){
            fetch('http://localhost:4567/bldnames')
                .then(response => response.json()
                    .then(data => {
                        this.setState({
                                isBldLoaded: true,
                                bldNames: data
                            }
                        );
                    })).catch(e => {
                        alert("Check Server");
                        window.close();
            })
        }
    }

    handleStartChange = (val: string) => {
        this.setState({
            start: val,
        })
    };

    handleEndChange = (val: string) => {
        this.setState({
            end: val,
        })
    };

    dropDown = (label: string, val: string, handler: Function) : JSX.Element => {
        return(
        <div>
            <label>
                {label}
                <select value={val} onChange={(e) => handler(e.target.value)}>
                    <option value="" key=""/>
                    {this.state.bldNames.map((bldName) => (
                        <option value={bldName} key={bldName}>{bldName}</option>
                    ))}
                </select>
            </label>
        </div>
        )
    }

    render() {
        return (
            <div id="edge-list">
                Select Buildings <br/>
                <div>
                    {this.dropDown("Start Building", this.state.start, this.handleStartChange)}
                    {this.dropDown("End Building", this.state.end, this.handleEndChange)}
                </div>

                <br/>
                <button onClick={() => {
                    this.onDrawClick();
                }}>Find</button>
                <button onClick={() => { this.onClearClick();
                }}>Clear</button>
            </div>
        );
    }
}

export default BldSelect;
