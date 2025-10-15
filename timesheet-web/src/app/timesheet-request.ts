export interface TimesheetRequest {
  email: string;
  date: string;        // yyyy-MM-dd
  loginTime?: string;  // HH:mm or ISO datetime
  logoutTime?: string;
}
