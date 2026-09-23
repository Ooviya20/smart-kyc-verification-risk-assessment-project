import { useEffect, useState } from "react";
import "./Customers.css";

function KycVerification({ setPage }) {

    const [documents, setDocuments] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [showForm, setShowForm] = useState(false);

    const [formData, setFormData] = useState({
        customerId: "",
        documentType: "PAN",
        documentNumber: "",
        documentStatus: "PENDING",
        expiryDate: ""
    });

    // Fetch KYC documents
    const fetchDocuments = () => {

        fetch("http://localhost:8080/api/kyc-documents")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch KYC documents");
                }

                return response.json();
            })
            .then((data) => {
                setDocuments(data);
            })
            .catch((error) => {
                console.error("Error fetching KYC documents:", error);
            });
    };

    // Fetch customers
    const fetchCustomers = () => {

        fetch("http://localhost:8080/api/customers")
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch customers");
                }

                return response.json();
            })
            .then((data) => {
                setCustomers(data);
            })
            .catch((error) => {
                console.error("Error fetching customers:", error);
            });
    };

    // Save KYC document
    const handleSaveDocument = (e) => {

        e.preventDefault();

        if (!formData.customerId) {
            alert("Please select a customer");
            return;
        }

        if (!formData.documentNumber.trim()) {
            alert("Please enter document number");
            return;
        }

        const requestData = {
            customerId: Number(formData.customerId),
            documentType: formData.documentType,
            documentNumber: formData.documentNumber.trim(),
            documentStatus: formData.documentStatus,
            expiryDate: formData.expiryDate || null
        };

        fetch("http://localhost:8080/api/kyc-documents", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestData)
        })
            .then((response) => {

                if (!response.ok) {
                    throw new Error("Failed to save KYC document");
                }

                return response.json();
            })
            .then(() => {

                alert("KYC document added successfully");

                setFormData({
                    customerId: "",
                    documentType: "PAN",
                    documentNumber: "",
                    documentStatus: "PENDING",
                    expiryDate: ""
                });

                setShowForm(false);

                fetchDocuments();
            })
            .catch((error) => {

                console.error("Error saving KYC document:", error);

                alert("Failed to save KYC document");
            });
    };

    // Verify / Reject KYC document
    const verifyDocument = (documentId, status) => {

        const reason =
            status === "VERIFIED"
                ? "Document details matched successfully"
                : "Document details do not match";

        fetch(`http://localhost:8080/api/kyc-documents/${documentId}/verify`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                status: status,
                reason: reason
            })
        })
            .then((response) => {

                if (!response.ok) {
                    throw new Error("Failed to update KYC verification");
                }

                return response.json();
            })
            .then(() => {

                alert(
                    status === "VERIFIED"
                        ? "KYC document verified successfully"
                        : "KYC document rejected"
                );

                // Reload documents
                fetchDocuments();
            })
            .catch((error) => {

                console.error("Error updating KYC verification:", error);

                alert("Failed to update KYC verification");
            });
    };

    // Initial loading
    useEffect(() => {

        fetchDocuments();
        fetchCustomers();

    }, []);

    return (
        <div className="dashboard">

            {/* Sidebar */}

            <aside className="sidebar">

                <h2>Smart KYC</h2>

                <p className="subtitle">
                    Risk Management
                </p>

                <nav>

                    <a onClick={() => setPage("dashboard")}>
                        Dashboard
                    </a>

                    <a onClick={() => setPage("customers")}>
                        Customers
                    </a>

                    <a className="active">
                        KYC Verification
                    </a>

                    <a
                        onClick={() => setPage("risk")}
                        style={{
                            display: "block",
                            width: "100%",
                            padding: "13px 15px",
                            borderRadius: "8px",
                            color: "#dbeafe",
                            cursor: "pointer"
                        }}
                    >
                        Risk Assessment
                    </a>

                    <a onClick={() => setPage("pep")}>
                        PEP Screening
                    </a>

                    <a onClick={() => setPage("audit")}>
                        Audit Logs
                    </a>

                </nav>

            </aside>

            {/* Main Content */}

            <main className="main-content">

                <div className="kyc-page">

                    {/* Header */}

                    <div className="customers-header">

                        <div>

                            <h1>
                                KYC Verification
                            </h1>

                            <p>
                                Review and verify customer KYC documents
                            </p>

                        </div>

                        <button
                            className="add-customer-btn"
                            onClick={() => setShowForm(true)}
                        >
                            + Add KYC Document
                        </button>

                    </div>

                    {/* Add KYC Document Form */}

                    {showForm && (

                        <div className="customers-card">

                            <h3>
                                Add KYC Document
                            </h3>

                            <form
                                className="customer-form"
                                onSubmit={handleSaveDocument}
                            >

                                {/* Customer + Document Type */}

                                <div className="form-row">

                                    <div className="form-group">

                                        <label>
                                            Customer
                                        </label>

                                        <select
                                            value={formData.customerId}
                                            onChange={(e) =>
                                                setFormData({
                                                    ...formData,
                                                    customerId: e.target.value
                                                })
                                            }
                                            required
                                        >

                                            <option value="">
                                                Select Customer
                                            </option>

                                            {customers.map((customer) => (

                                                <option
                                                    key={customer.id}
                                                    value={customer.id}
                                                >
                                                    {customer.fullName} - ID: {customer.id}
                                                </option>

                                            ))}

                                        </select>

                                    </div>

                                    <div className="form-group">

                                        <label>
                                            Document Type
                                        </label>

                                        <select
                                            value={formData.documentType}
                                            onChange={(e) =>
                                                setFormData({
                                                    ...formData,
                                                    documentType: e.target.value
                                                })
                                            }
                                        >

                                            <option value="PAN">
                                                PAN
                                            </option>

                                            <option value="AADHAAR">
                                                Aadhaar
                                            </option>

                                            <option value="PASSPORT">
                                                Passport
                                            </option>

                                            <option value="DRIVING_LICENSE">
                                                Driving License
                                            </option>

                                        </select>

                                    </div>

                                </div>

                                {/* Document Number + Status */}

                                <div className="form-row">

                                    <div className="form-group">

                                        <label>
                                            Document Number
                                        </label>

                                        <input
                                            type="text"
                                            placeholder="Enter document number"
                                            value={formData.documentNumber}
                                            onChange={(e) =>
                                                setFormData({
                                                    ...formData,
                                                    documentNumber: e.target.value
                                                })
                                            }
                                            required
                                        />

                                    </div>

                                    <div className="form-group">

                                        <label>
                                            Document Status
                                        </label>

                                        <select
                                            value={formData.documentStatus}
                                            onChange={(e) =>
                                                setFormData({
                                                    ...formData,
                                                    documentStatus: e.target.value
                                                })
                                            }
                                        >

                                            <option value="PENDING">
                                                PENDING
                                            </option>

                                            <option value="VALID">
                                                VALID
                                            </option>

                                            <option value="INVALID">
                                                INVALID
                                            </option>

                                        </select>

                                    </div>

                                </div>

                                {/* Expiry Date */}

                                <div className="form-group">

                                    <label>
                                        Expiry Date
                                    </label>

                                    <input
                                        type="date"
                                        value={formData.expiryDate}
                                        onChange={(e) =>
                                            setFormData({
                                                ...formData,
                                                expiryDate: e.target.value
                                            })
                                        }
                                    />

                                </div>

                                {/* Buttons */}

                                <div className="form-actions">

                                    <button
                                        type="button"
                                        className="cancel-btn"
                                        onClick={() => setShowForm(false)}
                                    >
                                        Cancel
                                    </button>

                                    <button
                                        type="submit"
                                        className="save-customer-btn"
                                    >
                                        Save Document
                                    </button>

                                </div>

                            </form>

                        </div>
                    )}

                    {/* KYC Documents */}

                    <div className="customers-card">

                        <div className="table-header">

                            <h3>
                                KYC Documents
                            </h3>

                            <span>
                                {documents.length} Documents
                            </span>

                        </div>

                        <div className="table-responsive">

                            <table className="customers-table">

                                <thead>

                                    <tr>

                                        <th>
                                            ID
                                        </th>

                                        <th>
                                            Customer Name
                                        </th>

                                        <th>
                                            Document Type
                                        </th>

                                        <th>
                                            Document Number
                                        </th>

                                        <th>
                                            Expiry Date
                                        </th>

                                        <th>
                                            Status
                                        </th>

                                        <th>
                                            Verification
                                        </th>

                                        <th>
                                            Action
                                        </th>

                                    </tr>

                                </thead>

                                <tbody>

                                    {documents.length === 0 ? (

                                        <tr>

                                            <td
                                                colSpan="8"
                                                style={{
                                                    textAlign: "center",
                                                    padding: "30px"
                                                }}
                                            >
                                                No KYC documents found
                                            </td>

                                        </tr>

                                    ) : (

                                        documents.map((document) => (

                                            <tr key={document.id}>

                                                <td>
                                                    #{document.id}
                                                </td>

                                                <td>
                                                    {document.customer?.fullName || "N/A"}
                                                </td>

                                                <td>
                                                    <strong>
                                                        {document.documentType}
                                                    </strong>
                                                </td>

                                                <td>
                                                    {document.documentNumber}
                                                </td>

                                                <td>

                                                    {document.expiryDate
                                                        ? new Date(
                                                            document.expiryDate
                                                        ).toLocaleDateString(
                                                            "en-GB",
                                                            {
                                                                day: "2-digit",
                                                                month: "short",
                                                                year: "numeric"
                                                            }
                                                        )
                                                        : "N/A"}

                                                </td>

                                                <td>

                                                    <span
                                                        className={`status-badge ${
                                                            document.documentStatus
                                                                ?.toLowerCase()
                                                        }`}
                                                    >
                                                        {document.documentStatus || "N/A"}
                                                    </span>

                                                </td>

                                                <td>

                                                    <span
                                                        className={`verification-badge ${
                                                            document.verificationStatus
                                                                ?.toLowerCase()
                                                        }`}
                                                    >
                                                        {document.verificationStatus || "PENDING"}
                                                    </span>

                                                </td>

                                                <td>

                                                    <div className="kyc-actions">

                                                        <button
                                                            className="verify-btn"
                                                            onClick={() =>
                                                                verifyDocument(
                                                                    document.id,
                                                                    "VERIFIED"
                                                                )
                                                            }
                                                            disabled={
                                                                document.verificationStatus ===
                                                                "VERIFIED"
                                                            }
                                                        >
                                                            ✓ Verify
                                                        </button>

                                                        <button
                                                            className="reject-btn"
                                                            onClick={() =>
                                                                verifyDocument(
                                                                    document.id,
                                                                    "REJECTED"
                                                                )
                                                            }
                                                            disabled={
                                                                document.verificationStatus ===
                                                                "REJECTED"
                                                            }
                                                        >
                                                            ✕ Reject
                                                        </button>

                                                    </div>

                                                </td>

                                            </tr>

                                        ))

                                    )}

                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>

            </main>

        </div>
    );
}

export default KycVerification;