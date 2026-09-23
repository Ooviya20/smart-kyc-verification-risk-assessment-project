import { useEffect, useState } from "react";
import "./Customers.css";

function Customers({ setPage }) {

    const [customers, setCustomers] = useState([]);
const [showForm, setShowForm] = useState(false);

const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phone: "",
    dateOfBirth: "",
    aadhaarNumber: "",
    panNumber: "",
    address: ""
});

const handleSaveCustomer = (e) => {

    e.preventDefault();

    fetch("http://localhost:8080/api/customers", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then((response) => {

            if (!response.ok) {
                throw new Error("Failed to create customer");
            }

            return response.json();
        })
        .then((newCustomer) => {

            setCustomers((previousCustomers) => [
                ...previousCustomers,
                newCustomer
            ]);

            setFormData({
                fullName: "",
                email: "",
                phone: "",
                dateOfBirth: "",
                aadhaarNumber: "",
                panNumber: "",
                address: ""
            });

            setShowForm(false);
        })
        .catch((error) => {
            console.error("Error creating customer:", error);
            alert("Failed to create customer");
        });
};

    useEffect(() => {

        fetch("http://localhost:8080/api/customers")
            .then((response) => response.json())
            .then((data) => {
                setCustomers(data);
            })
            .catch((error) => {
                console.error("Error fetching customers:", error);
            });

    }, []);

    console.log("Customers:", customers);

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

    <a className="active">
        Customers
    </a>

    <a onClick={() => setPage("kyc")}>
        KYC Verification
    </a>

    <a onClick={() => setPage("risk")}>
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

                <div className="customers-page">

                    {/* Header */}
                    <div className="customers-header">

                        <div>
                            <h1>Customers</h1>

                            <p>
                                Manage and review customer KYC information
                            </p>
                        </div>

                        <button
    className="add-customer-btn"
    onClick={() => setShowForm(true)}
>
    + Add Customer
</button>

                    </div>

{showForm && (
    <div className="customers-card">

        <h3>Add New Customer</h3>

        <form
            className="customer-form"
            onSubmit={handleSaveCustomer}
        >

            <div className="form-row">

                <div className="form-group">
                    <label>Full Name</label>

                    <input
                        type="text"
                        placeholder="Enter full name"
                        value={formData.fullName}
                        onChange={(e) =>
                            setFormData({
                                ...formData,
                                fullName: e.target.value
                            })
                        }
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Email</label>

                    <input
                        type="email"
                        placeholder="Enter email"
                        value={formData.email}
                        onChange={(e) =>
                            setFormData({
                                ...formData,
                                email: e.target.value
                            })
                        }
                        required
                    />
                </div>

            </div>

            <div className="form-row">

                <div className="form-group">
                    <label>Phone</label>

                    <input
    type="tel"
    placeholder="Enter 10 digit phone number"
    value={formData.phone}
    onChange={(e) => {
        const value = e.target.value.replace(/\D/g, "");

        if (value.length <= 10) {
            setFormData({
                ...formData,
                phone: value
            });
        }
    }}
    pattern="[0-9]{10}"
    maxLength="10"
    required
/>
                </div>

                <div className="form-group">
                    <label>Date of Birth</label>

                    <input
                        type="date"
                        value={formData.dateOfBirth}
                        onChange={(e) =>
                            setFormData({
                                ...formData,
                                dateOfBirth: e.target.value
                            })
                        }
                        required
                    />
                </div>

            </div>

            <div className="form-row">

                <div className="form-group">
                    <label>Aadhaar Number</label>

                    <input
    type="text"
    placeholder="Enter 12 digit Aadhaar number"
    value={formData.aadhaarNumber}
    onChange={(e) => {
        const value = e.target.value.replace(/\D/g, "");

        if (value.length <= 12) {
            setFormData({
                ...formData,
                aadhaarNumber: value
            });
        }
    }}
    pattern="[0-9]{12}"
    maxLength="12"
    required
/>
                </div>

                <div className="form-group">
                    <label>PAN Number</label>

                    <input
                        type="text"
                        placeholder="Enter PAN number"
                        value={formData.panNumber}
                        onChange={(e) =>
                            setFormData({
                                ...formData,
                                panNumber: e.target.value
                            })
                        }
                        required
                    />
                </div>

            </div>

            <div className="form-group">
                <label>Address</label>

                <textarea
                    placeholder="Enter address"
                    rows="3"
                    value={formData.address}
                    onChange={(e) =>
                        setFormData({
                            ...formData,
                            address: e.target.value
                        })
                    }
                    required
                ></textarea>
            </div>

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
                    Save Customer
                </button>

            </div>

        </form>

    </div>
)}


                    {/* Customer Table */}
                    <div className="customers-card">

                        <div className="table-header">

                            <h3>Customer List</h3>

                            <span>
                                {customers.length} Customers
                            </span>

                        </div>


                        <table className="customers-table">

                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Customer Name</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>KYC Status</th>
                                </tr>
                            </thead>


                            <tbody>

    {customers.length > 0 ? (

        customers.map((customer) => (

            <tr key={customer.id}>

                <td>
                    #{customer.id}
                </td>

                <td>
                    <strong>
                        {customer.fullName}
                    </strong>
                </td>

                <td>
                    {customer.email}
                </td>

                <td>
                    {customer.phone}
                </td>

                <td>
                    <span
                        className={`status-badge ${
                            customer.kycStatus
                                ? customer.kycStatus.toLowerCase()
                                : "pending"
                        }`}
                    >
                        {customer.kycStatus || "PENDING"}
                    </span>
                </td>

            </tr>

        ))

    ) : (

        <tr>
            <td colSpan="5" style={{ textAlign: "center" }}>
                No customers found
            </td>
        </tr>

    )}

</tbody>

                        </table>

                    </div>

                </div>

            </main>

        </div>
    );
}

export default Customers;