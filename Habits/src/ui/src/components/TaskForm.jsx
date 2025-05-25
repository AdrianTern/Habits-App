import { 
    Box,
    Button, 
    Dialog,
    DialogTitle,
    DialogActions,
    DialogContent,
    DialogContentText,
    Slide,
    Typography,
    TextField,
    Stack,
    IconButton, 
    Icon} from '@mui/material';
import CheckCircleRoundedIcon from '@mui/icons-material/CheckCircleRounded';
import RemoveCircleRoundedIcon from '@mui/icons-material/RemoveCircleRounded';
import AddCircleRoundedIcon from '@mui/icons-material/AddCircleRounded';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import { useState, useEffect } from 'react';

function TaskForm({ task, isOpen, onClose, onSave, onDelete }){
    const isCreate = task == null;
    const dialogTitle = (isCreate ? "Add a new task" : "Edit task");
    
    const [id, setId] = useState("");
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [dueDate, setDueDate] = useState(dayjs());
    const [isCompleted, setIsCompleted] = useState(false);

    useEffect(() => {
        if(isCreate){
            setTitle("");
            setDescription("");
            setDueDate(dayjs());
        }else{
            setId(task.id);
            setTitle(task.title);
            setDescription(task.description);
            setDueDate(dayjs(task.dueDate));
            setIsCompleted(task.isCompleted);
        }
    }, [isCreate, task]);
    
    const handleOnTitleChange = (event) => {
        setTitle(event.target.value);
    }

    const handleOnDescChange = (event) => {
        setDescription(event.target.value);
    }

    const handleOnDueDateChange = (newDate) => {
        setDueDate(newDate);
    }

    const handleOnSave = async () => {
        const dueDateString = dueDate.format('YYYY-MM-DD');
        const newTask = {
            id,
            title,
            description,
            dueDate: dueDateString,
            isCompleted
        }
        // Add/Update task
        onSave(isCreate, newTask);

        // Close dialog
        onClose();
    }

    const handleOnDelete = () => {
        onDelete(id);
        onClose();
    }

    return(
        <Box>
            <Dialog
                open={isOpen}
                onClose={onClose}
                fullWidth 
            >
                <DialogTitle>{dialogTitle}</DialogTitle>
                <DialogContent >
                    <Stack spacing={3}>
                        <TextField
                            onChange={handleOnTitleChange}
                            required
                            id='title'
                            name='title'
                            label="Title"
                            value={title}
                            variant='standard'
                        />
                        <TextField
                            onChange={handleOnDescChange}
                            fullWidth
                            id='description'
                            name='description'
                            label="Description"
                            value={description}
                            variant='standard'
                        />
                    </Stack>
                </DialogContent>
                <DialogActions 
                    sx={{
                        justifyContent: "space-between",
                        margin: "0.5rem 1rem 0.5rem 1rem",
                    }}>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DatePicker
                            label="Due Date"
                            value={dueDate}
                            sx={{ width: "10rem" }}
                            onChange={(newDate) => handleOnDueDateChange(newDate)}
                        />
                    </LocalizationProvider>
                    <Box display="flex" gap={1}>
                        <IconButton
                            aria-label='delete-task'
                            sx={{color: 'custom.red'}}
                            onClick={handleOnDelete}
                        >
                            {!isCreate && (<RemoveCircleRoundedIcon fontSize='large'/>)}
                        </IconButton>
                        <IconButton 
                            aria-label='update-task'
                            sx={{color: 'black'}}
                            onClick={handleOnSave}
                        >
                            {isCreate ? <AddCircleRoundedIcon fontSize='large'/> : <CheckCircleRoundedIcon fontSize='large'/>}
                        </IconButton>
                    </Box>
                </DialogActions>
            </Dialog>
        </Box>
    );

}
export default TaskForm;