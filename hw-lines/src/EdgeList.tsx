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

interface EdgeListProps {
    onChange(edges: Edge[] | null): void;  // called when a new edge list is ready
                                 // TODO: once you decide how you want to communicate the edges to the App, you should
                                 // change the type of edges so it isn't `any`
}

interface EdgeListState {
    val: string;
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
class EdgeList extends Component<EdgeListProps, EdgeListState> {

    constructor(props: EdgeListProps) {
        super(props);
        this.state = {
            val: "",
        }

    }

    onClearClick(){
        this.setState({
            val: '',
        })
        this.props.onChange(null);
    }

    onDrawClick() {
        let edges = this.validateEdge(this.state.val);
        if (edges === null || edges.length == 0){
            alert("Not a valid input\ne.g) 100 100 1400 2500 red\n");
            this.onClearClick();
            console.log("Draw Failed");
        } else {
            this.props.onChange(edges);
            console.log("Draw Success");
        }
    }

    validateEdge(val: string): Edge[] | null {
        let ret: Edge[] = [];

        let lines = val.split("\n");
        for (let i = 0; i < lines.length; i++) {
            let elems = lines[i].trim().split(" ");
            if (elems.length < 5)
                return null;

            let flag: boolean = true;
            for (let j = 0; j < 4; j++){
                if (parseInt(elems[j]) < 0 || parseInt(elems[j]) > 4000)
                    flag = false;
            }

            if (flag){
                let edge: Edge = {
                    x1: parseInt(elems[0]),
                    y1: parseInt(elems[1]),
                    x2: parseInt(elems[2]),
                    y2: parseInt(elems[3]),
                    color: elems[4]
                }

                ret.push(edge);
            } else {
                return null;
            }
        }

        return ret;
    }

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    onChange={(e) => {
                        this.setState({
                            val: e.target.value,
                        })
                    }}
                    value={this.state.val}
                /> <br/>
                <button onClick={() => { this.onDrawClick();
                }}>Draw</button>
                <button onClick={() => { this.onClearClick();
                }}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
