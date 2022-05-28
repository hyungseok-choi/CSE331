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

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import BldSelect  from "./BldSelect";
import Map from "./Map";

interface AppState {
    val: Edge[] | null;
}

type Edge = {
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    color: string;
}

class App extends Component<{}, AppState> {
    constructor(props: any) {
        super(props);
        this.state = {
            val: null
        };
    }

    render() {
        return (
            <div>
                <h1 id="app-title">Find shortest path!</h1>
                <div>
                    <Map
                        value = {this.state.val}
                    />
                </div>
                <BldSelect
                    onChange={(value) => {
                        this.setState({
                            val: value
                        });
                    }}
                />
            </div>
        );
    }

}

export default App;
