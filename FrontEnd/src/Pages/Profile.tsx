import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';

import "./Profile.css";

// Define the interface for the address object
interface Address {
  id: number;
  value: string;
}

const sendTokenToBackend = async (token: string) => {
    try {
        const response = await axios.post('http://localhost:9080/home/profile', { token });
        console.log('Response from backend:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error sending token to backend:', error);
    }
};

const Profile = () => {
    const location = useLocation();
    var {userToken} = location.state || {};
    console.log(userToken);
    
    // Define the state variables
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [addresses, setAddresses] = useState<Address[]>([]);

    useEffect(() => {
        if (userToken) {
            sendTokenToBackend(userToken)
            .then(data => {
                // Assuming 'data' has the structure you described
                setFirstName(data.firstName);
                setLastName(data.lastName);
                setEmail(data.email);
                setPhoneNumber(data.phoneNumber);
                setAddresses(data.addresses.map((address : string, index: number) => ({
                    id: index, 
                    value: address
                })));
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
        }
    }, [userToken]);


    // Function to add a new address
    const addAddress = () => {
        setAddresses([...addresses, { id: Date.now(), value: '' }]);
    };

    // Function to save the address with type annotations for parameters
    const saveAddress = (id: number, value: string) => {
        // Logic to save the address
    };

    // Function to delete an address
    const deleteAddress = (id: number) => {
        setAddresses(addresses.filter(address => address.id !== id));
    };
    return (
        <div className="profile-container">
            <div className="input-group">
                <label htmlFor="firstName">First Name</label>
                <input 
                    type="text" id="firstName" placeholder="Set first name" value={firstName}
                    onChange={(e) => setFirstName(e.target.value)} 
                />
                <button className="btn">Update</button>
            </div>

            <div className="input-group">
                <label htmlFor="lastName">Last Name</label>
                <input 
                    type="text" id="lastName" placeholder="Set Last Name" value={lastName}
                    onChange={(e) => setLastName(e.target.value)} />
                <button className="btn">Update</button>
            </div>

            <div className="input-group">
                <label htmlFor="email">Email Address</label>
                <input 
                    type="email" id="email" placeholder="Set Email Address" value={email}
                    onChange={(e) => setEmail(e.target.value)} />
                <button className="btn">Update</button>
            </div>

            <div className="input-group">
                <label htmlFor="phone">Phone Number</label>
                <input 
                    type="tel" id="phone" placeholder="Set Phone Number" value={phoneNumber}
                    onChange={(e) => setPhoneNumber(e.target.value)} />
                <button className="btn">Update</button>
            </div>

            <div className="addresses-container">
                <label className="address-label">Addresses</label>
                {addresses.map(address => (
                    <div className="address" key={address.id}>
                        <input
                            type="text" placeholder="Enter address" value={address.value}
                            onChange={(e) => {
                                const newAddresses = addresses.map(a =>
                                    a.id === address.id ? { ...a, value: e.target.value } : a
                                );
                                setAddresses(newAddresses);
                            }}
                        />
                        <button className="btn save-btn" onClick={() => saveAddress(address.id, address.value)}>Save</button>
                        <button className="btn remove-btn" onClick={() => deleteAddress(address.id)}>X</button>
                    </div>
                ))}
                <button className="btn add-btn" onClick={addAddress}>Add Address</button>
            </div>
        </div>
    );
};

export default Profile;
