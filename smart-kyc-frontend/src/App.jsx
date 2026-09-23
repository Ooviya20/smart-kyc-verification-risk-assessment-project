import { useState } from "react";

import Dashboard from "./Dashboard";
import Customers from "./Customers";
import KycVerification from "./KycVerification";
import RiskAssessment from "./RiskAssessment";
import PepScreening from "./PepScreening";
import AuditLogs from "./AuditLogs";

function App() {

    const [page, setPage] = useState("dashboard");

    console.log("Current page:", page);

    return (
        <>
            {page === "dashboard" && (
                <Dashboard setPage={setPage} />
            )}

            {page === "customers" && (
                <Customers setPage={setPage} />
            )}

            {page === "kyc" && (
                <KycVerification setPage={setPage} />
            )}

            {page === "risk" && (
                <RiskAssessment setPage={setPage} />
            )}

            {page === "pep" && (
               <PepScreening setPage={setPage} />
            )}

            {page === "audit" && (
              <AuditLogs setPage={setPage} />
            )}
        </>
    );
}

export default App;
