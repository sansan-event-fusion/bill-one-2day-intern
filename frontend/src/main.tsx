import { createRoot } from 'react-dom/client';
import React from "react";
import './index.css';
import {App} from "src/App";


const container = document.getElementById('root');
if (!container) throw new Error('root element not found');

const root = createRoot(container);
root.render(
    <App/>
);
